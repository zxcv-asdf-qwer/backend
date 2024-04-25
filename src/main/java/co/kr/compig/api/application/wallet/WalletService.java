package co.kr.compig.api.application.wallet;

import static co.kr.compig.api.domain.apply.QApply.*;
import static co.kr.compig.api.domain.order.QCareOrder.*;
import static co.kr.compig.api.domain.packing.QPacking.*;
import static co.kr.compig.global.utils.CalculateUtil.*;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import co.kr.compig.api.application.member.MemberService;
import co.kr.compig.api.application.system.EncryptKeyService;
import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.packing.Packing;
import co.kr.compig.api.domain.wallet.Wallet;
import co.kr.compig.api.domain.wallet.WalletRepository;
import co.kr.compig.api.domain.wallet.WalletRepositoryCustom;
import co.kr.compig.api.presentation.order.request.CareOrderCalculateRequest;
import co.kr.compig.api.presentation.wallet.request.WalletCreateRequest;
import co.kr.compig.api.presentation.wallet.request.WalletSearchRequest;
import co.kr.compig.api.presentation.wallet.response.WalletDetailResponse;
import co.kr.compig.api.presentation.wallet.response.WalletResponse;
import co.kr.compig.api.presentation.wallet.response.WalletResponseWithSecret;
import co.kr.compig.global.code.ApplyStatus;
import co.kr.compig.global.code.ExchangeType;
import co.kr.compig.global.code.TransactionType;
import co.kr.compig.global.crypt.AES256;
import co.kr.compig.global.dto.pagination.PageResponse;
import co.kr.compig.global.error.exception.NotExistDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WalletService {

	private final WalletRepository walletRepository;
	private final WalletRepositoryCustom walletRepositoryCustom;
	private final MemberService memberService;
	private final JPAQueryFactory jpaQueryFactory;
	private final EncryptKeyService encryptKeyService;

	public Long createWalletAdmin(WalletCreateRequest walletCreateRequest) {
		Member member = memberService.getMemberById(walletCreateRequest.getMemberId());

		// 가장 최근의 Wallet 가져오기
		Wallet lastWallet = walletRepository.findTopByMemberOrderByCreatedAndModified_CreatedOnDesc(member)
			.orElse(null);
		// 이전 잔액
		int previousBalance = (lastWallet != null) ? lastWallet.getBalance() : 0;

		// 새 잔액 계산
		int newBalance;
		if (lastWallet != null && walletCreateRequest.getTransactionType().equals(TransactionType.CREDIT)) {
			newBalance = previousBalance + walletCreateRequest.getAmount();
		} else if (lastWallet != null && walletCreateRequest.getTransactionType().equals(TransactionType.DEBIT)) {
			newBalance = previousBalance - walletCreateRequest.getAmount();
		} else {
			// lastWallet이 null일 경우, 이전 거래가 없으므로 transactionAmount만으로 잔액 설정
			newBalance = walletCreateRequest.getAmount();
		}

		Wallet wallet = Wallet.builder()
			.member(member)
			.packing(null)
			.transactionType(walletCreateRequest.getTransactionType()) // 입금, 출금
			.exchangeType(ExchangeType.HAND) // 수기
			.transactionAmount(walletCreateRequest.getAmount()) //수기입력 금액
			.balance(newBalance) //잔액
			.description(walletCreateRequest.getDescription())
			.build();
		return walletRepository.save(wallet).getId();
	}

	public void createWallet(Packing nowPacking, TransactionType transactionType,
		ExchangeType exchangeType, String description) {
		Member member = jpaQueryFactory
			.select(apply.member) // Member 객체를 선택
			.from(apply) // Apply 엔티티에서 시작
			.join(apply.careOrder, careOrder) // Apply와 CareOrder를 조인
			.join(careOrder.packages, packing) // CareOrder와 Packing을 조인
			.where(apply.applyStatus.eq(ApplyStatus.MATCHING_COMPLETE), // 상태가 MATCHING_COMPLETE인 Apply 필터링
				careOrder.id.eq(nowPacking.getCareOrder().getId())) // 특정 orderId를 가진 CareOrder 필터링
			.fetchOne(); // 결과 가져오기 (단일 결과 예상)

		if (member == null) {
			throw new NotExistDataException(); // 또는 적절한 예외 처리
		}

		// 가장 최근의 Wallet 가져오기
		Wallet lastWallet = walletRepository.findTopByMemberOrderByCreatedAndModified_CreatedOnDesc(member)
			.orElse(null);
		// 이전 잔액
		int previousBalance = (lastWallet != null) ? lastWallet.getBalance() : 0;
		//간병하루 - 간병인 수수료
		CareOrderCalculateRequest calculateRequest = CareOrderCalculateRequest.builder()
			.amount(nowPacking.getAmount())
			.periodType(nowPacking.getPeriodType())
			.partTime(nowPacking.getPartTime())
			.build();
		int transactionAmount = calculatePriceOneDay(calculateRequest) - nowPacking.getSettle().getPartnerFees();
		// 새 잔액 계산
		int newBalance;
		if (lastWallet != null && transactionType.equals(TransactionType.CREDIT)) {
			newBalance = previousBalance + transactionAmount;
		} else if (lastWallet != null && transactionType.equals(TransactionType.DEBIT)) {
			newBalance = previousBalance - transactionAmount;
		} else {
			// lastWallet이 null일 경우, 이전 거래가 없으므로 transactionAmount만으로 잔액 설정
			newBalance = transactionAmount;
		}

		Wallet wallet = Wallet.builder()
			.member(member)
			.packing(nowPacking)
			.transactionType(transactionType) // 입금
			.exchangeType(exchangeType) // 자동
			.transactionAmount(transactionAmount) //간병하루 - 간병인 수수료
			.balance(newBalance) //잔액
			.description(description)
			.build();
		walletRepository.save(wallet);
		// TODO 스케쥴러 createBy
		// TODO 마지막 날이면 orderstatus ORDER_COMPLETE 으로 변경 해주기
	}

	@Transactional(readOnly = true)
	public Page<WalletResponse> getWalletPage(WalletSearchRequest walletSearchRequest) {
		return walletRepositoryCustom.findPage(walletSearchRequest);
	}

	@Transactional(readOnly = true)
	public Page<WalletDetailResponse> getExchangeHandWalletPage(WalletSearchRequest walletSearchRequest) {
		return walletRepositoryCustom.getExchangeHandWalletPage(walletSearchRequest);
	}

	@Transactional(readOnly = true)
	public ResponseEntity<PageResponse> getExchangeOneDayWalletPage(WalletSearchRequest walletSearchRequest) {
		Page<Wallet> page = walletRepositoryCustom.getExchangeOneDayWalletPage(walletSearchRequest);
		if(CollectionUtils.isEmpty(page.getContent())) {
			return PageResponse.noResult();
		}
		AES256 aes256 = encryptKeyService.getEncryptKey();
		List<WalletResponseWithSecret> responses =
			page.get().map(wallet -> {
				CareOrderCalculateRequest calculateRequest = CareOrderCalculateRequest.builder()
					.amount(wallet.getPacking().getAmount())
					.periodType(wallet.getPacking().getPeriodType())
					.partTime(wallet.getPacking().getPartTime())
					.build();
				String accountName;
				String accountNumber;

				try {
					accountName = aes256.decrypt(wallet.getMember().getAccount().getAccountName(),
						wallet.getMember().getAccount().getIv());
				} catch (Exception e) {
					accountName = null;
					log.error("Decryption error for account name: {}", e.getMessage());
				}

				try {
					accountNumber = aes256.decrypt(wallet.getMember().getAccount().getAccountNumber(),
						wallet.getMember().getAccount().getIv());
				} catch (Exception e) {
					accountNumber = null;
					log.error("Decryption error for account number: {}", e.getMessage());
				}
				return WalletResponseWithSecret.builder()
					.userNm(wallet.getMember().getUserNm())
					.price(calculatePriceOneDay(calculateRequest))
					.partnerFee(wallet.getPacking().getSettle().getPartnerFees())
					.transactionAmount(wallet.getTransactionAmount())
					.orderId(wallet.getPacking().getCareOrder().getId())
					.accountName(accountName)
					.jumin(wallet.getMember().getJumin1() + "-" + wallet.getMember().getJumin2())
					.bankName(wallet.getMember().getAccount() != null ?  wallet.getMember().getAccount().getBankName() : null)
					.accountNumber(accountNumber)
					.build();
			}).collect(Collectors.toList());
		return PageResponse.ok(responses.stream().toList(), page.getPageable().getOffset(), page.getTotalElements());
	}

	@Transactional(readOnly = true)
	public WalletResponse getWallet(String memberId) {
		Member member = memberService.getMemberById(memberId);
		return WalletResponse.builder()
			.partnerMemberResponse(member.toPartnerMemberResponse())
			.walletDetailResponsesList(member.getWallets()
				.stream()
				.map(Wallet::toWalletDetailResponse)
				.collect(Collectors.toList())
			).build();
	}

}
