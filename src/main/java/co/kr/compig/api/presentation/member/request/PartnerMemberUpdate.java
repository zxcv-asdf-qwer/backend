package co.kr.compig.api.presentation.member.request;

import java.util.List;

import co.kr.compig.api.domain.code.CareerCode;
import co.kr.compig.api.domain.code.DomesticForeignCode;
import co.kr.compig.api.domain.code.GenderCode;
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
public class PartnerMemberUpdate {

	@NotBlank
	private String newUserPw;   // 새 비밀번호
	@NotBlank
	private String chkUserPw;   // 새 비밀번호 확인
	@NotBlank
	private String telNo; // 휴대폰번호

	//간병인
	@NotNull
	@Parameter(description = "간병인")
	private GenderCode gender; // 성별
	@NotBlank
	@Parameter(description = "간병인")
	private String address1; //주소
	@NotBlank
	@Parameter(description = "간병인")
	private String address2; //주소
	@NotNull
	@Parameter(description = "간병인")
	private DomesticForeignCode domesticForeignCode; //외국인 내국인
	@NotNull
	@Parameter(description = "간병인")
	private CareerCode careerCode; //신입 경력
	@NotNull
	@Parameter(description = "간병인")
	private Integer careStartYear; //근무 시작 연도
	@NotBlank
	@Parameter(description = "간병인")
	private String introduce; //자기소개

	//[간병인&&보호자] 회원 공통
	@NotNull
	@Parameter(description = "[간병인&&보호자]회원 공통")
	private boolean marketingEmail; // 이메일 수신동의
	@NotNull
	@Parameter(description = "[간병인&&보호자]회원 공통")
	private boolean marketingAppPush; // 앱 푸시알림 수신동의
	@NotNull
	@Parameter(description = "[간병인&&보호자]회원 공통")
	private boolean marketingKakao; // 알림톡 수신동의
	@NotNull
	@Parameter(description = "[간병인&&보호자]회원 공통")
	private boolean marketingSms; // 문자 수신동의
	@NotNull
	private List<String> groupKeys;
}
