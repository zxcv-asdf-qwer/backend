package co.kr.compig.api.presentation.social.request;

import co.kr.compig.api.domain.code.GenderCode;
import co.kr.compig.api.domain.code.MemberRegisterType;
import co.kr.compig.api.domain.code.UserType;
import co.kr.compig.api.domain.member.Member;
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
	private String telNo; //연락처
	@NotBlank
	private MemberRegisterType memberRegisterType; //소셜로그인 타입
	@NotBlank
	private UserType userType; //유저타입
	@NotBlank
	private GenderCode gender; //성별
	@NotBlank
	private String birthday; //생년월일

	private boolean marketingEmail; // 이메일 수신동의
	private boolean marketingAppPush; // 앱 푸시알림 수신동의
	private boolean marketingKakao; // 알림톡 수신동의
	private boolean marketingSms; // 문자 수신동의
	@NotBlank
	private String ci; //나이스 본인인증 ci 값

	public Member converterEntity() {
		return Member.builder()
			.userNm(this.userNm)
			.email(this.email)
			.telNo(this.telNo)
			.ci(this.ci)
			.userPw(this.email + this.memberRegisterType + "compig")
			.memberRegisterType(this.memberRegisterType)
			.userType(this.userType)
			.gender(this.gender)
			.jumin1(this.birthday)
			.build();
	}
}
