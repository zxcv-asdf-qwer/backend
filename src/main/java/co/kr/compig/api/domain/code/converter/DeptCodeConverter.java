package co.kr.compig.api.domain.code.converter;

import co.kr.compig.api.domain.code.DeptCode;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DeptCodeConverter extends AbstractBaseEnumConverter<DeptCode, String> {

	@Override
	protected String getEnumName() {
		return "DeptCode";
	}

	@Override
	protected DeptCode[] getValueList() {
		return DeptCode.values();
	}
}
