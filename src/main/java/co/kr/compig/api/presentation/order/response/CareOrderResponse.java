package co.kr.compig.api.presentation.order.response;

import co.kr.compig.api.domain.code.CareOrderRegisterType;
import co.kr.compig.api.domain.code.LocationType;
import co.kr.compig.api.domain.code.UserType;
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
public class CareOrderResponse extends BaseAudit {

	private Long id; // 공고 ID
	private UserType userType; // 회원구분
	private CareOrderRegisterType careOrderRegisterType; // 등록 구분
	private String userNm; // 보호자 이름
	private String telNo; // 전화번호
	private LocationType locationType; // 간병 장소 종료
	private String address1; // 간병 주소
}
