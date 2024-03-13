package co.kr.compig.api.domain.code.converter;

import co.kr.compig.api.domain.code.MenuTypeCode;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MenuTypeCodeConverter extends AbstractBaseEnumConverter<MenuTypeCode, String> {

	@Override
	protected String getEnumName() {
		return "MenuTypeCode";
	}

	@Override
	protected MenuTypeCode[] getValueList() {
		return MenuTypeCode.values();
	}
}
