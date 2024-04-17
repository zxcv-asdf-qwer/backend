package co.kr.compig.api.presentation.social.request;

import co.kr.compig.global.code.ApplicationType;
import co.kr.compig.global.code.MemberRegisterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SocialLoginRequest {

	private User user; //apple //{ "name": { "firstName": string, "lastName": string }, "email": string }
	private String code;
	private String token;
	private MemberRegisterType memberRegisterType;
	private ApplicationType applicationType;

	@Getter
	public static class User {

		private Name name;
		private String email;
	}

	@Getter
	public static class Name {

		private String firstName;
		private String lastName;
	}
}
