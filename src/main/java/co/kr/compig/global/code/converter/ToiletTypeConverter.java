package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.ToiletType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(ToiletType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class ToiletTypeConverter extends AbstractBaseEnumConverter<ToiletType, String> {
	@Override
	protected String getEnumName() {
		return "ToiletType";
	}

	@Override
	protected ToiletType[] getValueList() {
		return ToiletType.values();
	}
}
