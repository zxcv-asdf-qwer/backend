package co.kr.compig.api.presentation.member.request;

import org.hibernate.validator.constraints.Length;

import co.kr.compig.api.domain.member.Member;
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
public class NoMemberCreate {

	@NotBlank
	@Length(min = 2, max = 100)
	@Pattern(regexp = "^[\\sㄱ-ㅎ가-힣A-Za-z0-9_-]{2,100}$")
	private String userNm; // 사용자 명

	private String telNo; // 휴대폰번호

	public Member convertEntity() {
		return Member.builder()
			.userNm(this.userNm)
			.telNo(this.telNo)
			.build();
	}
}
