package co.kr.compig.api.presentation.social.response;

import co.kr.compig.api.domain.code.MemberRegisterType;
import co.kr.compig.api.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class SocialUserResponse {

	private MemberRegisterType memberRegisterType;
	private String sub;//social userId
	private String email;
	private String name;
	private String gender;
	private String birthday;

	public Member convertEntity() {
		return Member.builder()
			.userNm(this.name)
			.email(this.email)
			.userPw(this.email + this.memberRegisterType)
			.memberRegisterType(this.memberRegisterType)
			.build();
	}
}
