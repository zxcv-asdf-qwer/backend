package co.kr.compig.api.presentation.member.response;

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
public class UserMainSearchResponse extends BaseAudit {
	private String memberId; //keycloak ID
	private String userNm; //사용자 명
	private String telNo; //연락처
}
