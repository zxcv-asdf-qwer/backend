package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.TransactionType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(TransactionType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class TransactionTypeConverter extends AbstractBaseEnumConverter<TransactionType, String> {

	@Override
	protected String getEnumName() {
		return "TransactionType";
	}

	@Override
	protected TransactionType[] getValueList() {
		return TransactionType.values();
	}
}
