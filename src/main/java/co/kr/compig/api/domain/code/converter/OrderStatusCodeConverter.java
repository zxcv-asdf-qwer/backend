package co.kr.compig.api.domain.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.api.domain.code.OrderStatusCode;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(OrderStatusCode.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class OrderStatusCodeConverter extends AbstractBaseEnumConverter<OrderStatusCode, String> {

	@Override
	protected String getEnumName() {
		return "OrderStatusCode";
	}

	@Override
	protected OrderStatusCode[] getValueList() {
		return OrderStatusCode.values();
	}

}
