package co.kr.compig.api.domain.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import co.kr.compig.api.domain.code.UseYn;

public interface MemberRepository extends JpaRepository<Member, String>,
	QuerydslPredicateExecutor<Member> {

	Optional<Member> findByUserId(String userId);

	Optional<Member> findByEmailAndUseYn(String email, UseYn useYn);

	Optional<Member> findByUserNmAndTelNo(String userNm, String telNo);

	List<Member> findByUserNmOrTelNo(String userNm, String telNo);

}
