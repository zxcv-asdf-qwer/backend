package co.kr.compig.api.domain.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.api.domain.code.TermsType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(TermsType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class TermsTypeConverter extends AbstractBaseEnumConverter<TermsType, String> {

	@Override
	protected String getEnumName() {
		return "TermsType";
	}

	@Override
	protected TermsType[] getValueList() {
		return TermsType.values();
	}
}
