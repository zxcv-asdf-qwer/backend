package co.kr.compig.common.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.common.code.BoardType;
import co.kr.compig.common.code.EncryptType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(BoardType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class EncryptTypeConverter extends AbstractBaseEnumConverter<EncryptType, String> {

	@Override
	protected String getEnumName() {
		return "EncryptType";
	}

	@Override
	protected EncryptType[] getValueList() {
		return EncryptType.values();
	}
}
