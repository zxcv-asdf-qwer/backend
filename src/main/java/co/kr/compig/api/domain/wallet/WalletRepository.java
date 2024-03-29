package co.kr.compig.api.domain.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface WalletRepository extends JpaRepository<Wallet, Long>,
	QuerydslPredicateExecutor<Wallet> {
}
