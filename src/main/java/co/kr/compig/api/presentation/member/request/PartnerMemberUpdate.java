package co.kr.compig.api.presentation.member.request;

import java.util.List;

import co.kr.compig.api.presentation.account.request.AccountUpdateRequest;
import co.kr.compig.global.code.CareerCode;
import co.kr.compig.global.code.DiseaseCode;
import co.kr.compig.global.code.DomesticForeignCode;
import co.kr.compig.global.code.GenderCode;
import co.kr.compig.global.code.ToiletType;
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

	private String newUserPw;   // 새 비밀번호
	private String chkUserPw;   // 새 비밀번호 확인

	@NotBlank
	private String telNo; // 휴대폰번호

	//간병인
	@NotNull
	@Parameter(description = "성별")
	private GenderCode gender; // 성별
	@NotBlank
	@Parameter(description = "주소")
	private String address1; //주소
	@NotBlank
	@Parameter(description = "주소")
	private String address2; //주소
	@NotNull
	@Parameter(description = "외국인 내국인")
	private DomesticForeignCode domesticForeignCode; //외국인 내국인
	@NotNull
	@Parameter(description = "신입 경력")
	private CareerCode careerCode; //신입 경력
	@NotNull
	@Parameter(description = "근무 시작 연도")
	private Integer careStartYear; //근무 시작 연도
	@NotBlank
	@Parameter(description = "자기소개")
	private String introduce; //자기소개

	@NotBlank
	private String jumin1; // 주민등록번호 앞자리
	private String jumin2; // 주민등록번호 뒷자리

	private AccountUpdateRequest accountUpdateRequest;

	private List<DiseaseCode> diseaseNms; // 진단명
	private List<ToiletType> selfToiletAvailabilities; // 대소변 해결 여부

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
