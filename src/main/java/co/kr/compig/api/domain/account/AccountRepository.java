package co.kr.compig.api.domain.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import co.kr.compig.api.domain.member.Member;

public interface AccountRepository extends JpaRepository<Account, Long>,
	QuerydslPredicateExecutor<Account> {

	Optional<Account> findByMember(Member member);

	Boolean existsByMember(Member member);
}
