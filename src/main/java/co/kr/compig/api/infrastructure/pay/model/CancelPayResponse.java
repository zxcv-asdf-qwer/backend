package co.kr.compig.api.infrastructure.pay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelPayResponse {

	private String pgTid; //거래고유번호
	private String resultCode; //결과코드
	private String resultMsg; //결과메시지
	private String pgApprovalAmt; //취소금액
	private String pgAppDate; //취소일
	private String pgAppTime; //취소시간
	private String stateCd; //취소상태
	private String currency; //-- 해외카드결제 환불시 //통화
}
