package co.kr.compig.global.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentType implements BaseEnumCode<String> {
	CARD("CARD", "카드 결제"),
	ACCOUNT("ACCOUNT", "가상 계좌");

	private final String code;
	private final String desc;
}
