package co.kr.compig.api.social.dto;

import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class SocialUserResponse {

  private MemberRegisterType memberRegisterType;
  private String id;
  private String email;
  private String name;
  private String gender;
  private String birthday;

  public Member convertEntity() {
    return Member.builder()
        .userId(this.email)
        .userNm("socialName")
        .email(this.email)
        .userPw(this.email + this.memberRegisterType)
        .memberRegisterType(this.memberRegisterType)
        .build();
  }
}
