package co.kr.compig.api.member.dto;

import co.kr.compig.common.code.MemberRegisterType;
import co.kr.compig.common.code.UseYn;
import co.kr.compig.domain.member.Member;
import co.kr.compig.domain.member.MemberGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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

  @Builder.Default
  private UseYn useYn = UseYn.Y; // 사용유무

  private String telNo; // 휴대폰번호

  private MemberRegisterType memberRegisterType;
  private String providerId;
  private String providerUsername;
  @NotNull
  private List<String> groupKeys;

  public Member convertEntity() {

    Member member = Member.builder()
        .userNm(this.userNm)
        .userId(this.userId)
        .userPw(this.userPw)
        .telNo(this.telNo)
        .useYn(this.useYn)
        .memberRegisterType(this.memberRegisterType)
        .build();

    groupKeys.forEach(groupKey ->
            member.addGroups(
                MemberGroup.builder().groupKey(groupKey).build()
            )
        );
    return member;
  }
}
