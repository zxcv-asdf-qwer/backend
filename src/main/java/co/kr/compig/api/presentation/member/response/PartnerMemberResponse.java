package co.kr.compig.api.presentation.member.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import co.kr.compig.api.presentation.account.response.AccountDetailResponse;
import co.kr.compig.api.presentation.member.model.GroupDto;
import co.kr.compig.global.code.DiseaseCode;
import co.kr.compig.global.code.DomesticForeignCode;
import co.kr.compig.global.code.GenderCode;
import co.kr.compig.global.code.MemberRegisterType;
import co.kr.compig.global.code.ToiletType;
import co.kr.compig.global.dto.BaseAudit;
import io.swagger.v3.oas.annotations.Parameter;
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
public class PartnerMemberResponse extends BaseAudit {

	private String memberId; //keycloak ID
	@Parameter(description = "사용자 명")
	private String userNm; //사용자 명
	@Parameter(description = "연락처")
	private String telNo; //연락처
	@Parameter(description = "나이")
	private int age; //나이
	@Parameter(description = "이메일")
	private String email; //이메일
	@Parameter(description = "회원가입 유형")
	private MemberRegisterType memberRegisterType; //회원가입 유형
	@Parameter(description = "성별")
	private GenderCode gender; //성별
	@Parameter(description = "가입일자")
	private LocalDate registerDate; //가입일자
	@Parameter(description = "s3 path")
	private String picture; //s3 path
	@Parameter(description = "근무 시작 연도")
	private Integer careStartYear; //근무 시작 연도
	@Parameter(description = "경력")
	private Integer careerYear; //경력
	@Parameter(description = "매칭수")
	private int matchingCount; //매칭수
	@Parameter(description = "리뷰 별 평균")
	private int starAverage; //리뷰 별 평균
	@Parameter(description = "주소1")
	private String address1; //주소1
	@Parameter(description = "상세주소")
	private String address2; //상세주소
	@Parameter(description = "외국인 내국인")
	private DomesticForeignCode domesticForeignCode; //외국인 내국인
	@Parameter(description = "주민등록번호 앞자리")
	private String jumin1; // 주민등록번호 앞자리
	@Parameter(description = "계좌")
	private AccountDetailResponse accountDetailResponse; // 계좌번호
	@Parameter(description = "자기소개")
	private String introduce; //자기소개
	@Parameter(description = "진단명")
	private List<DiseaseCode> diseaseNms; // 진단명
	@Parameter(description = "대소변 해결 여부")
	private List<ToiletType> selfToiletAvailabilities; // 대소변 해결 여부
	@Parameter(description = "이메일 수신동의")
	private boolean marketingEmail; // 이메일 수신동의
	@Parameter(description = "앱 푸시알림 수신동의")
	private boolean marketingAppPush; // 앱 푸시알림 수신동의
	@Parameter(description = "알림톡 수신동의")
	private boolean marketingKakao; // 알림톡 수신동의
	@Parameter(description = "문자 수신동의")
	private boolean marketingSms; // 문자 수신동의
	private Set<GroupDto> groups;
}
