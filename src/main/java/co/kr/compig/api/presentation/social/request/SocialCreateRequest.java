package co.kr.compig.api.presentation.social.request;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.global.code.MemberRegisterType;
import co.kr.compig.global.code.UserType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialCreateRequest {

	@NotBlank
	private String socialId; //소셜 로그인 id
	@NotBlank
	private String userNm; //사용자 이름 ex)홍 길동
	@NotBlank
	private String email; //이메일
	@NotBlank
	private MemberRegisterType memberRegisterType; //소셜로그인 타입
	@NotBlank
	private UserType userType; //유저타입
	@NotBlank
	private String telNo; // 핸드폰 번호

	private boolean marketingEmail; // 이메일 수신동의
	private boolean marketingAppPush; // 앱 푸시알림 수신동의
	private boolean marketingKakao; // 알림톡 수신동의
	private boolean marketingSms; // 문자 수신동의

	public Member converterEntity() {
		return Member.builder()
			.userNm(this.userNm)
			.userId(this.email)
			.email(this.email)
			.userPw(this.email + this.memberRegisterType + "compig")
			.memberRegisterType(this.memberRegisterType)
			.userType(this.userType)
			.telNo(this.telNo)
			.build();
	}
}
