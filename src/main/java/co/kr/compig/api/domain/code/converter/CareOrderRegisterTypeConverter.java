package co.kr.compig.api.domain.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.api.domain.code.CareOrderRegisterType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(CareOrderRegisterType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class CareOrderRegisterTypeConverter extends AbstractBaseEnumConverter<CareOrderRegisterType, String> {

	@Override
	protected String getEnumName() {
		return "CareOrderRegisterType";
	}

	@Override
	protected CareOrderRegisterType[] getValueList() {
		return CareOrderRegisterType.values();
	}
}
