package co.kr.compig.api.presentation.member.response;

import co.kr.compig.api.domain.code.GenderCode;
import co.kr.compig.api.domain.code.MemberRegisterType;
import co.kr.compig.api.domain.code.UseYn;
import co.kr.compig.api.domain.code.UserType;
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
public class MemberPageResponse extends BaseAudit {

	private String memberId; //keycloak ID
	private String userId; //user ID
	private String userNm; // 사용자 명
	private String email; // 이메일
	private String telNo; // 연락처
	private GenderCode gender; // 성별
	private UseYn useYn; // 사용유무
	private UserType userType; //사용자 타입
	private MemberRegisterType memberRegisterType; // 회원가입 유형
}
