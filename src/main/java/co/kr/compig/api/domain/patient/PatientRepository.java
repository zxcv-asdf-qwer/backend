package co.kr.compig.api.domain.patient;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.domain.member.NoMember;

public interface PatientRepository extends JpaRepository<Patient, Long>,
	QuerydslPredicateExecutor<Patient> {
	Boolean existsByMemberAndName(Member member, String name);

	List<Patient> findAllByMember(Member member);

	List<Patient> findAllByNoMember(NoMember noMember);
}
