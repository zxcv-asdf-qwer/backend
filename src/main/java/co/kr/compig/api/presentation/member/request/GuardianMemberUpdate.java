package co.kr.compig.api.presentation.member.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuardianMemberUpdate {

	@NotBlank
	private String userNm; // 사용자 이름
	@NotBlank
	private String telNo; // 휴대폰번호

	//[간병인&&보호자] 회원 공통
	@NotNull
	@Parameter(description = "[간병인&&보호자]회원 공통")
	private boolean marketingEmail; // 이메일 수신동의
	@NotNull
	@Parameter(description = "[간병인&&보호자]회원 공통")
	private boolean marketingAppPush; // 앱 푸시알림 수신동의
	@NotNull
	@Parameter(description = "[간병인&&보호자]회원 공통")
	private boolean marketingKakao; // 알림톡 수신동의
	@NotNull
	@Parameter(description = "[간병인&&보호자]회원 공통")
	private boolean marketingSms; // 문자 수신동의

}
