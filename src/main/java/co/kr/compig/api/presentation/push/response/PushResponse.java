package co.kr.compig.api.presentation.push.response;

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
public class PushResponse extends BaseAudit {
	private Long pushId;
	private String message; //내용
	private String receiverMemberId; //수신자 멤버 ID
	private String receiverMemberName; //수신자 이름
}
