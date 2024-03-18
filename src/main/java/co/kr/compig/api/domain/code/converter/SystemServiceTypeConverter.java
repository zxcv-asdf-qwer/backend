package co.kr.compig.api.domain.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.api.domain.code.SystemServiceType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(SystemServiceType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class SystemServiceTypeConverter extends
	AbstractBaseEnumConverter<SystemServiceType, String> {

	@Override
	protected String getEnumName() {
		return "SystemServiceType";
	}

	@Override
	protected SystemServiceType[] getValueList() {
		return SystemServiceType.values();
	}
}
