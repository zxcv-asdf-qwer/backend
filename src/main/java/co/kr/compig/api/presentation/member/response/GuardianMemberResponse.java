package co.kr.compig.api.presentation.member.response;

import java.time.LocalDate;

import co.kr.compig.global.code.MemberRegisterType;
import co.kr.compig.global.code.MemberType;
import co.kr.compig.global.dto.BaseAudit;
import io.swagger.v3.oas.annotations.Parameter;
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
	private MemberType memberType; //회원 비회원
	@Parameter(description = "이메일 수신동의")
	private boolean marketingEmail; // 이메일 수신동의
	@Parameter(description = "앱 푸시알림 수신동의")
	private boolean marketingAppPush; // 앱 푸시알림 수신동의
	@Parameter(description = "알림톡 수신동의")
	private boolean marketingKakao; // 알림톡 수신동의
	@Parameter(description = "문자 수신동의")
	private boolean marketingSms; // 문자 수신동의

}
