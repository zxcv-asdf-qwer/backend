package co.kr.compig.api.presentation.social.request;

import co.kr.compig.api.domain.code.GenderCode;
import co.kr.compig.api.domain.code.MemberRegisterType;
import co.kr.compig.api.domain.code.UserType;
import co.kr.compig.api.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialCreateRequest {

	private String socialId; //소셜 로그인 id
	private String userNm; //사용자 이름 ex)홍 길동
	private String email; //이메일
	private String telNo; //연락처
	private String ci; //나이스 본인인증 ci 값
	private MemberRegisterType memberRegisterType; //회원가입 타입
	private UserType userType; //유저타입
	private GenderCode gender; //성별
	private String birthday; //생년월일

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
