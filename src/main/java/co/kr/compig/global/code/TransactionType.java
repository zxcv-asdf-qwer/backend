package co.kr.compig.global.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransactionType implements BaseEnumCode<String> {

	CREDIT("CREDIT", "입금"),
	DEBIT("DEBIT", "출금");

	private final String code;
	private final String desc;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDesc() {
		return desc;
	}

}
