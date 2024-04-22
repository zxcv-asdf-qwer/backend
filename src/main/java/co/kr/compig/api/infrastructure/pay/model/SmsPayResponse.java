package co.kr.compig.api.infrastructure.pay.model;

import java.time.LocalDate;

import co.kr.compig.api.domain.payment.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsPayResponse {

	private String amt; //결제요청금액
	private String buyerEmail; //결제자 이메일 주소
	private String buyerName; //결제자 이름
	private String buyerTel; //결제자 휴대폰번호
	private String dutyFreeAmt; //면세 금액
	private String goodsName; //상품명
	private String mid; //상점아이디
	private String moid; //상점주문번호 (고유값)
	private String payExpDate; //SMS 결제 마감기한
	private String orderUrl; //결제 URL
	private String resultCode; //결과코드
	private String resultMsg; //결과메시지
	private String userId; //결제자 회원 ID

	public Payment toEntity(int totalPrice) {
		return Payment.builder()
			.goodsName(this.goodsName)
			.price(totalPrice)//보호자 수수료 적용한 금액(보호자가 지불해야 하는 금액)(간병일 전체)
			.moid(this.moid)
			.orderUrl(this.orderUrl)
			.buyerName(this.buyerName)
			.buyerTel(this.buyerTel)
			.buyerEmail(this.buyerEmail)
			.payExpDate(LocalDate.now().atTime(23, 59, 59))
			.payRequestResultCode(this.resultCode)
			.build();
	}
}
