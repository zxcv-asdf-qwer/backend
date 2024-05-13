package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.MemberType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(MemberType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class MemberTypeConverter extends AbstractBaseEnumConverter<MemberType, String> {

	@Override
	protected String getEnumName() {
		return "MemberType";
	}

	@Override
	protected MemberType[] getValueList() {
		return MemberType.values();
	}
}
