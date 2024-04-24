package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.ExchangeType;
import co.kr.compig.global.code.SmsType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(SmsType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class ExchangeTypeConverter extends AbstractBaseEnumConverter<ExchangeType, String> {

	@Override
	protected String getEnumName() {
		return "ExchangeType";
	}

	@Override
	protected ExchangeType[] getValueList() {
		return ExchangeType.values();
	}
}
