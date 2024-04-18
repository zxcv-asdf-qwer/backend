package co.kr.compig.api.presentation.member.request;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.global.code.MemberRegisterType;
import co.kr.compig.global.code.UseYn;
import co.kr.compig.global.code.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuardianMemberCreate {

	@NotBlank
	@Length(min = 2, max = 100)
	@Pattern(regexp = "^[\\sㄱ-ㅎ가-힣A-Za-z0-9_-]{2,100}$")
	private String userNm; // 사용자 명

	@NotBlank
	@Email
	private String email; // 이메일

	@NotBlank
	private String userPw; // 사용자 비밀번호

	@NotBlank
	private String telNo; // 연락처

	private boolean marketingEmail; // 이메일 수신동의
	private boolean marketingAppPush; // 앱 푸시알림 수신동의
	private boolean marketingKakao; // 알림톡 수신동의
	private boolean marketingSms; // 문자 수신동의

	public Member convertEntity() {
		return Member.builder()
			.userId(this.email)
			.userNm(this.userNm)
			.userPw(this.userPw)
			.telNo(this.telNo)
			.email(this.email)
			.useYn(UseYn.Y)
			.userType(UserType.GUARDIAN)
			.memberRegisterType(MemberRegisterType.GENERAL)
			.marketingEmailDate(this.marketingEmail ? LocalDate.now() : null)
			.marketingAppPushDate(this.marketingAppPush ? LocalDate.now() : null)
			.marketingKakaoDate(this.marketingKakao ? LocalDate.now() : null)
			.marketingSmsDate(this.marketingSms ? LocalDate.now() : null)
			.build();
	}
}
