package co.kr.compig.api.member.dto;

import co.kr.compig.common.code.CareerCode;
import co.kr.compig.common.code.DomesticForeignCode;
import co.kr.compig.common.code.GenderCode;
import co.kr.compig.common.code.IsYn;
import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.common.code.UseYn;
import co.kr.compig.common.code.UserType;
import co.kr.compig.common.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MemberResponse extends BaseAudit {

  private String userNm; // 사용자 명
  private String email; // 이메일
  private String userPw; // 사용자 비밀번호
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
