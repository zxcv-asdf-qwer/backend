package co.kr.compig.api.presentation.member.request;

import org.hibernate.validator.constraints.Length;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.global.code.DeptCode;
import co.kr.compig.global.code.MemberRegisterType;
import co.kr.compig.global.code.UseYn;
import co.kr.compig.global.code.UserType;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberCreate {

	@NotBlank
	@Length(min = 2, max = 100)
	@Pattern(regexp = "^[\\sㄱ-ㅎ가-힣A-Za-z0-9_-]{2,100}$")
	private String userNm; // 사용자 명

	@NotBlank
	@Length(min = 3, max = 15)
	@Pattern(regexp = "^[A-Za-z0-9_]{3,15}$")
	private String userId; // 사용자 아이디

	@NotBlank
	private String userPw; // 사용자 비밀번호

	private String telNo; // 휴대폰번호

	@Parameter(description = "DEVELOPER, OPERATION")
	@NotNull
	private DeptCode deptCode; //부서코드

	public Member convertEntity() {
		return Member.builder()
			.userNm(this.userNm)
			.userId(this.userId)
			.userPw(this.userPw)
			.telNo(this.telNo)
			.useYn(UseYn.Y)
			.userType(this.deptCode.equals(DeptCode.DEVELOPER) ? UserType.SYS_ADMIN : UserType.SYS_USER)
			.deptCode(this.deptCode)
			.memberRegisterType(MemberRegisterType.GENERAL)
			.build();
	}
}
