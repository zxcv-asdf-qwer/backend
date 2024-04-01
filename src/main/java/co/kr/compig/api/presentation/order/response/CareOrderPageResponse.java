package co.kr.compig.api.presentation.order.response;

import java.time.LocalDateTime;

import co.kr.compig.global.code.LocationType;
import co.kr.compig.global.code.OrderStatus;
import co.kr.compig.global.code.PeriodType;
import co.kr.compig.global.dto.BaseAudit;
import io.swagger.v3.oas.annotations.Parameter;
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
public class CareOrderPageResponse extends BaseAudit {

	private Long orderId; // 공고 ID
	@Parameter(description = "보호자 ID")
	private String memberId; // 보호자Id
	@Parameter(description = "보호자명")
	private String userNm; // 보호자명
	@Parameter(description = "보호자번호")
	private String telNo; // 보호자번호
	@Parameter(description = "간병 장소 종류")
	private LocationType locationType; // 간병 장소 종류
	@Parameter(description = "간병 장소 주소")
	private String address1; // 간병 장소 주소
	@Parameter(description = "간병 장소 상세 주소")
	private String address2; // 간병 장소 상세 주소
	@Parameter(description = "병원 명")
	private String hospitalNm; // 병원 명
	@Parameter(description = "시작 날짜")
	private LocalDateTime startDateTime; // 시작 날짜
	@Parameter(description = "종료 날짜")
	private LocalDateTime endDateTime; // 종료 날짜
	@Parameter(description = "진짜 종료 날짜")
	private LocalDateTime realEndDateTime; // 진짜 종료 날짜
	@Parameter(description = "공고 상태")
	private OrderStatus orderStatus; // 공고 상태
	@Parameter(description = "시간제, 기간제")
	private PeriodType periodType; // 시간제, 기간제
	@Parameter(description = "금액 //보호자들이 입력한 금액, 수수료 계산전")
	private Integer amount; //금액 //보호자들이 입력한 금액, 수수료 계산전
	@Parameter(description = "지원자 수")
	private Integer applyCount; //지원자 수
	@Parameter(description = "메모 수")
	private Integer memoCount; //메모 수
}
