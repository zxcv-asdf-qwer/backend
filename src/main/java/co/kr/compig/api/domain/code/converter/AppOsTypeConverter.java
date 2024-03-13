package co.kr.compig.api.domain.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.api.domain.code.AppOsType;
import co.kr.compig.api.domain.code.BoardType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(BoardType.class)
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
