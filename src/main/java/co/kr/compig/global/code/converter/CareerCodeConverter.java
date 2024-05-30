package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.CareerCode;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(CareerCode.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class CareerCodeConverter extends AbstractBaseEnumConverter<CareerCode, String> {

	@Override
	protected String getEnumName() {
		return "CareerCode";
	}

	@Override
	protected CareerCode[] getValueList() {
		return CareerCode.values();
	}
}
