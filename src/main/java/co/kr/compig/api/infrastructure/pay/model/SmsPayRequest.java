package co.kr.compig.api.infrastructure.pay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsPayRequest {

	private String mid; //상점아이디
	private String moid; //상점주문번호 (고유값)
	private String goodsName; //상품명
	private String amt; //결제요청금액
	private String buyerName; //결제자 이름
	private String buyerTel; //결제자 휴대폰번호
	@Builder.Default
	private String svcPrdtCd = "03"; //Sub 지불수단 //선택

	private String dutyFreeAmt; //면세 금액 //선택
	private String buyerEmail; //결제자 이메일 주소 //선택
	private String payExpDate; //SMS 결제 마감기한 //선택
	private String userId; //결제자 회원 ID //선택
}
