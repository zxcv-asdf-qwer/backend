package co.kr.compig.api.domain.patient;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import co.kr.compig.api.domain.member.Member;

public interface PatientRepository extends JpaRepository<Patient, Long>,
	QuerydslPredicateExecutor<Patient> {
	Boolean existsByMemberAndPatientNm(Member member, String patientNm);

	List<Patient> findAllByMember(Member member);
}
