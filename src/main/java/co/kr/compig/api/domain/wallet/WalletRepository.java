package co.kr.compig.api.domain.wallet;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import co.kr.compig.api.domain.member.Member;

public interface WalletRepository extends JpaRepository<Wallet, Long>,
	QuerydslPredicateExecutor<Wallet> {
	// Member의 마지막 Wallet을 찾기
	Optional<Wallet> findTopByMemberOrderByCreatedAndUpdated_CreatedOnDesc(Member member);
}
