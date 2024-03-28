package co.kr.compig.api.domain.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.api.domain.code.ApplyStatusCode;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(ApplyStatusCode.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class ApplyStatusConverter extends AbstractBaseEnumConverter<ApplyStatusCode, String> {

	@Override
	protected String getEnumName() {
		return "MatchingStatus";
	}

	@Override
	protected ApplyStatusCode[] getValueList() {
		return ApplyStatusCode.values();
	}
}
