package co.kr.compig.api.domain.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.api.domain.code.CareOrderProcessType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(CareOrderProcessType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class CareOrderProcessTypeConverter extends AbstractBaseEnumConverter<CareOrderProcessType, String> {

	@Override
	protected String getEnumName() {
		return "CareOrderProcessType";
	}

	@Override
	protected CareOrderProcessType[] getValueList() {
		return CareOrderProcessType.values();
	}
}
