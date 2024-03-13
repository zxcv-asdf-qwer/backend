package co.kr.compig.api.domain.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.api.domain.code.SmsType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(SmsType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class SmsTypeConverter extends AbstractBaseEnumConverter<SmsType, String> {

	@Override
	protected String getEnumName() {
		return "SmsType";
	}

	@Override
	protected SmsType[] getValueList() {
		return SmsType.values();
	}
}
