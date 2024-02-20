package co.kr.compig.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long>,
    QuerydslPredicateExecutor<MemberGroup> {

}
