package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.SmsTemplateType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(SmsTemplateType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class SmsTemplateTypeConverter extends AbstractBaseEnumConverter<SmsTemplateType, String> {

	@Override
	protected String getEnumName() {
		return "SmsTemplateType";
	}

	@Override
	protected SmsTemplateType[] getValueList() {
		return SmsTemplateType.values();
	}
}
