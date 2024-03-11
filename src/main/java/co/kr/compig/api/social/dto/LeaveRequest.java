package co.kr.compig.api.social.dto;

import co.kr.compig.common.code.MemberRegisterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LeaveRequest {

  private String leaveReason; //탈퇴사유
  private String code;
  private MemberRegisterType memberRegisterType;
}
