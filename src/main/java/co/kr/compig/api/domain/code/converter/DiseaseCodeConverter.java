package co.kr.compig.api.domain.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.api.domain.code.DiseaseCode;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(DiseaseCode.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class DiseaseCodeConverter extends AbstractBaseEnumConverter<DiseaseCode, String> {
	@Override
	protected String getEnumName() {
		return "DiseaseCode";
	}

	@Override
	protected DiseaseCode[] getValueList() {
		return DiseaseCode.values();
	}
}
