package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.LocationType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(LocationType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class LocationTypeConverter extends AbstractBaseEnumConverter<LocationType, String> {

	@Override
	protected String getEnumName() {
		return "LocationType";
	}

	@Override
	protected LocationType[] getValueList() {
		return LocationType.values();
	}
}
