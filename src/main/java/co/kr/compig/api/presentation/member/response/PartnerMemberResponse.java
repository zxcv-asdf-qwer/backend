package co.kr.compig.api.presentation.member.response;

import java.time.LocalDate;

import co.kr.compig.global.code.MemberRegisterType;
import co.kr.compig.global.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class PartnerMemberResponse extends BaseAudit {

	private String memberId; //keycloak ID
	private String userNm; //사용자 명
	private String telNo; //연락처
	private int age; //나이
	private String email; //이메일
	private MemberRegisterType memberRegisterType; //회원가입 유형
	private String gender; //성별
	private LocalDate registerDate; //가입일자
	private String picture; //s3 path
	private Integer career; //경력
	private int matchingCount; //매칭수
	private String starAverage; //리뷰 별 평균
	private String address1; //주소1
	private String address2; //상세주소
	private String introduce; //자기소개

}
