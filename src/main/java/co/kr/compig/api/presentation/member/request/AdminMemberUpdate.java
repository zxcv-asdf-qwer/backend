package co.kr.compig.api.presentation.member.request;

import co.kr.compig.api.domain.code.DeptCode;
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
public class AdminMemberUpdate {

	@NotBlank
	@Parameter(description = "형식: 홍 길동")
	private String userNm; //이름
	@NotBlank
	private String newUserPw;   // 새 비밀번호
	@NotBlank
	private String chkUserPw;   // 새 비밀번호 확인
	@NotBlank
	private String telNo; // 휴대폰번호
	@NotNull
	@Parameter(description = "관리자 DEVELOPER, OPERATION")
	private DeptCode deptCode; //부서코드

}
