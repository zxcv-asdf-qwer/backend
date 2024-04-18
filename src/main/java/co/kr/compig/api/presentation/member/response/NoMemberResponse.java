package co.kr.compig.api.presentation.member.response;

import co.kr.compig.global.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NoMemberResponse extends BaseAudit {

	private Long noMemberId; //비회원 ID
	private String userNm; // 사용자 명
	private String telNo; // 휴대폰번호

}
