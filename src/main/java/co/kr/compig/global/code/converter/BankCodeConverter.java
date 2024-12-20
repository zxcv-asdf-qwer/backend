package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.BankCode;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(BankCode.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class BankCodeConverter extends AbstractBaseEnumConverter<BankCode, String> {

	@Override
	protected String getEnumName() {
		return "BankCode";
	}

	@Override
	protected BankCode[] getValueList() {
		return BankCode.values();
	}
}
