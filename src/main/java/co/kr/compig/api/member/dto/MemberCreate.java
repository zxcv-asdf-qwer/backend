package co.kr.compig.api.member.dto;

import co.kr.compig.common.code.IsYn;
import co.kr.compig.common.code.UseYn;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreate {

  @NotBlank
  @Length(min = 3, max = 15)
  @Pattern(regexp = "^[A-Za-z0-9_]{3,15}$")
  private String userId; // 사용자 아이디

  @NotBlank
  private String userPw; // 사용자 비밀번호

  @NotBlank
  @Email
  private String email; // 이메일

  @NotBlank
  @Length(min = 2, max = 100)
  @Pattern(regexp = "^[\\sㄱ-ㅎ가-힣A-Za-z0-9_-]{2,100}$")
  private String userNm; // 사용자 명

  @Builder.Default
  private UseYn useYn = UseYn.Y; // 사용유무
  @Builder.Default
  private IsYn realNameYn = IsYn.N; // 실명 확인 여부

  private LocalDate marketingEmailDate;  // 이메일 수신동의 날짜
  private LocalDate marketingSmsDate;  // 문자 수신동의 날짜
  private LocalDate marketingKakaoDate;  // 알림톡 수신동의 날짜
  private LocalDate marketingAppDate; // 앱 푸시알림 수신동의 날짜
  private LocalDate marketingThirdDate; // 제 3자 정보 제공 동의 날짜
}
