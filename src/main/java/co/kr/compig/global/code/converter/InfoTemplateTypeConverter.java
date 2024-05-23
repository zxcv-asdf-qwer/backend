package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.infoTemplateType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(infoTemplateType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class InfoTemplateTypeConverter extends AbstractBaseEnumConverter<infoTemplateType, String> {

	@Override
	protected String getEnumName() {
		return "infoTemplateType";
	}

	@Override
	protected infoTemplateType[] getValueList() {
		return infoTemplateType.values();
	}
}
