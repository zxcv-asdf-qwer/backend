package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.OrderStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(OrderStatus.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class OrderStatusConverter extends AbstractBaseEnumConverter<OrderStatus, String> {

	@Override
	protected String getEnumName() {
		return "OrderStatus";
	}

	@Override
	protected OrderStatus[] getValueList() {
		return OrderStatus.values();
	}

}
