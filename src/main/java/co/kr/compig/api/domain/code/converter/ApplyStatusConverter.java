package co.kr.compig.api.domain.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.api.domain.code.ApplyStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(ApplyStatus.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class ApplyStatusConverter extends AbstractBaseEnumConverter<ApplyStatus, String> {

	@Override
	protected String getEnumName() {
		return "ApplyStatus";
	}

	@Override
	protected ApplyStatus[] getValueList() {
		return ApplyStatus.values();
	}
}
