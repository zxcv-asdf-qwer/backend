package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.DomesticForeignCode;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(DomesticForeignCode.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class DomesticForeignCodeConverter extends AbstractBaseEnumConverter<DomesticForeignCode, String> {

	@Override
	protected String getEnumName() {
		return "DomesticForeignCode";
	}

	@Override
	protected DomesticForeignCode[] getValueList() {
		return DomesticForeignCode.values();
	}
}
