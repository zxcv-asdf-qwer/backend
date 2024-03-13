package co.kr.compig.api.domain.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EncryptType implements BaseEnumCode<String> {
	AES256("AES256", "AES256"),
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
