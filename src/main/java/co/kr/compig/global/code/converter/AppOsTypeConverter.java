package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.AppOsType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(AppOsType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class AppOsTypeConverter extends AbstractBaseEnumConverter<AppOsType, String> {

	@Override
	protected String getEnumName() {
		return "AppOsType";
	}

	@Override
	protected AppOsType[] getValueList() {
		return AppOsType.values();
	}
}
