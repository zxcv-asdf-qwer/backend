package co.kr.compig.api.domain.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.api.domain.code.PeriodType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(PeriodType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class PeriodTypeConverter extends AbstractBaseEnumConverter<PeriodType, String> {

	@Override
	protected String getEnumName() {
		return "PeriodType";
	}

	@Override
	protected PeriodType[] getValueList() {
		return PeriodType.values();
	}
}
