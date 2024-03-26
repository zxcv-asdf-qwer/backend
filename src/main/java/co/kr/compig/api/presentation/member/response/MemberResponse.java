package co.kr.compig.api.presentation.member.response;

import org.springframework.web.multipart.MultipartFile;

import co.kr.compig.api.domain.code.CareerCode;
import co.kr.compig.api.domain.code.DomesticForeignCode;
import co.kr.compig.api.domain.code.GenderCode;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.api.domain.code.MemberRegisterType;
import co.kr.compig.api.domain.code.UseYn;
import co.kr.compig.api.domain.code.UserType;
import co.kr.compig.global.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class MemberResponse extends BaseAudit {

	private String memberId; //keycloak ID
	private String userId; //user ID
	private String userNm; // 사용자 명
	private String email; // 이메일
	private String telNo; // 연락처
	private GenderCode gender; // 성별
	private UseYn useYn; // 사용유무
	private UserType userType; //사용자 타입
	private MemberRegisterType memberRegisterType; // 회원가입 유형
	private String address1; //주소
	private String address2; //주소
	private MultipartFile picture; //프로필사진
	private DomesticForeignCode domesticForeignCode; //외국인 내국인
	private CareerCode careerCode; //신입 경력
	private Integer careStartYear; //근무 시작 연도
	private String introduce; //자기소개
	private boolean marketingEmail; // 이메일 수신동의
	private boolean marketingAppPush; // 앱 푸시알림 수신동의
	private boolean marketingKakao; // 알림톡 수신동의
	private boolean marketingSms; // 문자 수신동의
	private IsYn realNameYn; // 실명 확인 여부
}