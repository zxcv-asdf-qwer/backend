package co.kr.compig.api.presentation.member.response;

import java.time.LocalDate;

import co.kr.compig.api.domain.code.MemberRegisterType;
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
public class GuardianMemberResponse extends BaseAudit {

	private String memberId; //keycloak ID
	private String userNm; //사용자 명
	private String telNo; //연락처
	private String email; //이메일
	private MemberRegisterType memberRegisterType; //회원가입 유형
	private LocalDate registerDate; //가입일자

}
