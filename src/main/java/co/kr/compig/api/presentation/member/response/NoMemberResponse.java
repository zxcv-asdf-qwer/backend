package co.kr.compig.api.presentation.member.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoMemberResponse {

	private Long noMemberId; //비회원 ID
	private String userNm; // 사용자 명

	private String telNo; // 휴대폰번호

}
