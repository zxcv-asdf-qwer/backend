package co.kr.compig.domain.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import co.kr.compig.domain.member.Member;

public interface AccountRepository extends JpaRepository<Account, Long>,
	QuerydslPredicateExecutor<Account> {

	Optional<Account> findByMember(Member member);

	Boolean existsByMember(Member member);
}
