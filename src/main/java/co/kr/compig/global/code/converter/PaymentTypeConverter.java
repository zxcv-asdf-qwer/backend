package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.BoardType;
import co.kr.compig.global.code.PaymentType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(BoardType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class PaymentTypeConverter extends AbstractBaseEnumConverter<PaymentType, String> {
	@Override
	protected String getEnumName() {
		return "PaymentType";
	}

	@Override
	protected PaymentType[] getValueList() {
		return PaymentType.values();
	}
}
