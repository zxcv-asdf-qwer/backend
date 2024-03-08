package co.kr.compig.domain.account;

import co.kr.compig.domain.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AccountRepository extends JpaRepository<Account, Long>,
    QuerydslPredicateExecutor<Account> {

  Optional<Account> findByMember(Member member);
}
