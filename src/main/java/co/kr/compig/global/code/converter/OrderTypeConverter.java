package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.OrderType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(OrderType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class OrderTypeConverter extends AbstractBaseEnumConverter<OrderType, String> {

	@Override
	protected String getEnumName() {
		return "OrderType";
	}

	@Override
	protected OrderType[] getValueList() {
		return OrderType.values();
	}

}
