package co.kr.compig.api.infrastructure.pay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelPayRequest {

	private String mid = "arstest01m"; //이노페이 상점아이디
	private String tid = "arstest01m01011801181105238578"; //거래고유번호
	private String svcCd = "01"; //결제 구분
	private String cancelAmt = "1000"; //취소금액
	private String cancelMsg = "test"; //취소사유
	private String cancelPwd = "123456"; //취소비밀번호
	private String refundBankCd = "004"; // -- 가상계좌 환불시 //은행코드
	private String refundAcctNo = "12345671234567"; //-- 가상계좌 환불시 //계좌번호
	private String refundAcctNm = "홍길동"; //-- 가상계좌 환불시 //예금주
	private String currency = "USD"; //-- 해외카드결제 환불시 //통화
	private String partialCancelCode = "0"; //취소범위
}
