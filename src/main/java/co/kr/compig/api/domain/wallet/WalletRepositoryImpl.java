package co.kr.compig.api.domain.wallet;

import static co.kr.compig.api.domain.member.QMember.*;
import static co.kr.compig.api.domain.wallet.QWallet.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.presentation.wallet.request.WalletSearchRequest;
import co.kr.compig.api.presentation.wallet.response.WalletDetailResponse;
import co.kr.compig.api.presentation.wallet.response.WalletResponse;
import co.kr.compig.global.code.ExchangeType;
import co.kr.compig.global.code.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class WalletRepositoryImpl implements WalletRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<WalletResponse> findPage(WalletSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPQLQuery<Long> subQuery = JPAExpressions
			.select(wallet.id.max())
			.from(wallet)
			.where(wallet.member.id.eq(member.id))
			.groupBy(wallet.member.id);

		JPAQuery<Member> query = jpaQueryFactory
			.selectFrom(member)
			.leftJoin(member.wallets, wallet)
			.on(wallet.id.in(subQuery))
			.where(predicate, predicate.and(member.userType.eq(UserType.PARTNER)))
			.orderBy(wallet.balance.desc()); //현재 잔액(가장 최근 지갑 정보) 높은순으로 정렬

		Pageable pageable = request.pageable();

		List<Member> members = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		List<WalletResponse> responses = members.stream().map(member1 -> {
			// 최신 지갑 ID를 찾습니다.
			Optional<Long> latestWalletId = member1.getWallets().stream()
				.map(Wallet::getId)
				.max(Long::compare);
			return WalletResponse.builder()
				.partnerMemberResponse(member1.toPartnerMemberResponse())
				.walletDetailResponsesList(member1.getWallets()
					.stream()
					.filter(wallet -> latestWalletId.isPresent() && wallet.getId().equals(latestWalletId.get()))
					.map(Wallet::toWalletDetailResponse)
					.collect(Collectors.toList())
				).build();
		}).collect(Collectors.toList());

		JPAQuery<Long> countQuery = jpaQueryFactory
			.select(member.count())
			.leftJoin(member.wallets, wallet)
			.on(wallet.id.in(subQuery))
			.where(predicate, predicate.and(member.userType.eq(UserType.PARTNER)));

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}

	private BooleanExpression createPredicate(WalletSearchRequest request) {
		BooleanExpression predicate = Expressions.asBoolean(true).isTrue();
		if (request.getUserNm() != null) {
			predicate = predicate.and(wallet.member.userNm.eq(request.getUserNm()));
		}
		if (request.getFromCreatedOn() != null) {
			predicate = predicate.and(
				member.createdAndModified.createdOn.goe(request.getFromCreatedOn())); //크거나 같고(.goe)
		}

		if (request.getToCreatedOn() != null) {
			predicate = predicate.and(member.createdAndModified.createdOn.loe(request.getToCreatedOn())); //작거나 같은(.loe)
		}
		return predicate;
	}

	@Override
	public Page<WalletDetailResponse> getExchangeHandWalletPage(WalletSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<Wallet> query = jpaQueryFactory
			.selectFrom(wallet)
			.leftJoin(wallet.member, member)  // 지갑에서 회원으로 조인
			.where(wallet.member.userType.eq(UserType.PARTNER), wallet.exchangeType.eq(ExchangeType.HAND),
				predicate) // 추가적인 필터 조건
			.orderBy(wallet.id.desc());  // 지갑 ID에 따라 내림차순 정렬

		Pageable pageable = request.pageable();

		List<Wallet> wallets = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		List<WalletDetailResponse> responses = wallets.stream()
			.map(Wallet::toWalletDetailResponse)
			.collect(Collectors.toList());

		JPAQuery<Long> countQuery = jpaQueryFactory
			.select(wallet.count())
			.leftJoin(wallet.member, member)  // 지갑에서 회원으로 조인
			.where(wallet.member.userType.eq(UserType.PARTNER), wallet.exchangeType.eq(ExchangeType.HAND),
				predicate); // 추가적인 필터 조건

		return PageableExecutionUtils.getPage(responses, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<Wallet> getExchangeOneDayWalletPage(WalletSearchRequest request) {
		BooleanExpression predicate = createPredicate(request);

		JPAQuery<Wallet> query = jpaQueryFactory
			.selectFrom(wallet)
			.leftJoin(wallet.member, member)  // 지갑에서 회원으로 조인
			.where(wallet.member.userType.eq(UserType.PARTNER), wallet.exchangeType.eq(ExchangeType.AUTO),
				predicate) // 추가적인 필터 조건
			.orderBy(wallet.id.desc());  // 지갑 ID에 따라 내림차순 정렬

		Pageable pageable = request.pageable();

		List<Wallet> wallets = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = jpaQueryFactory
			.select(wallet.count())
			.leftJoin(wallet.member, member)  // 지갑에서 회원으로 조인
			.where(wallet.member.userType.eq(UserType.PARTNER), wallet.exchangeType.eq(ExchangeType.AUTO),
				predicate); // 추가적인 필터 조건

		return PageableExecutionUtils.getPage(wallets, pageable, countQuery::fetchOne);
	}
}
