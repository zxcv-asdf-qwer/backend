package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EncryptTarget implements BaseEnumCode<String> {
	ACCOUNT("ACCOUNT", "계좌번호"),
	;

	private final String code;
	private final String desc;

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getDesc() {
		return this.desc;
	}
}
