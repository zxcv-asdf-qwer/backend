package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.ContentsType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(ContentsType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class ContentsTypeConverter extends AbstractBaseEnumConverter<ContentsType, String> {

	@Override
	protected String getEnumName() {
		return "ContentsType";
	}

	@Override
	protected ContentsType[] getValueList() {
		return ContentsType.values();
	}
}
