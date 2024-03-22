package co.kr.compig.api.presentation.social.request;

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

	public Member converterEntity() {
		return Member.builder()
			.email(this.email)
			.telNo(this.telNo)
			.userNm(this.userNm)
			.ci(this.ci)
			.build();
	}
}
