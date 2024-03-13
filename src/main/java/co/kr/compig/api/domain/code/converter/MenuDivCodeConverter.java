package co.kr.compig.api.domain.code.converter;

import co.kr.compig.api.domain.code.MenuDivCode;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MenuDivCodeConverter extends AbstractBaseEnumConverter<MenuDivCode, String> {

	@Override
	protected String getEnumName() {
		return "MenuDivCode";
	}

	@Override
	protected MenuDivCode[] getValueList() {
		return MenuDivCode.values();
	}
}
