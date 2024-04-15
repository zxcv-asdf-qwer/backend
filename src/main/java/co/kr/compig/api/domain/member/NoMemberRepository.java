package co.kr.compig.api.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface NoMemberRepository extends JpaRepository<NoMember, String>,
	QuerydslPredicateExecutor<NoMember> {

}
