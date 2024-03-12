package co.kr.compig.common.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.common.code.BoardType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(BoardType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class BoardTypeConverter extends AbstractBaseEnumConverter<BoardType, String> {

	@Override
	protected String getEnumName() {
		return "BoardType";
	}

	@Override
	protected BoardType[] getValueList() {
		return BoardType.values();
	}
}
