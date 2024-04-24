package co.kr.compig.api.presentation.apply.response;

import co.kr.compig.global.code.ApplyStatus;
import co.kr.compig.global.code.GenderCode;
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
public class ApplyDetailResponse extends BaseAudit {
	private Long applyId; // 지원 ID
	private String memberId; // 멤버 ID
	private String userNm; //이름
	private GenderCode gender; //성별
	private int age; //나이
	private String telNo; //연락처
	private Long careOrderId; // 공고 ID
	private ApplyStatus applyStatus; // 지원 상태

}
