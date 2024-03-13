package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuestionType implements BaseEnumCode<String> {
	PAYMENT_ORDER("PAY", "주문/결제"); //주문 결제
	private final String code;
	private final String desc;
}
