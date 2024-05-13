package co.kr.compig.api.presentation.packing.response;

import co.kr.compig.global.code.LocationType;
import co.kr.compig.global.code.PeriodType;
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
public class FackingDetailResponse extends BaseAudit {

	private Long fackingId;
	private Long careOrderId;
	private Integer amount; //금액 //보호자들이 입력한 금액, 수수료 계산전
	private PeriodType periodType; // 시간제, 기간제
	private Integer partTime; //파트타임 시간 시간제 일 경우 필수
	private String partnerNm; // 간병인 이름
	private String partnerTelNo; // 간병인 전화번호
	private LocationType locationType; // 간병 장소 종류
	private String addressCd; // 간병 장소 우편 번호
	private String address1; // 간병 장소 주소
	private String address2; // 간병 장소 상세 주소
}
