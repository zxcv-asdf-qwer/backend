package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.BoardType;
import co.kr.compig.global.code.ReportType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(BoardType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class ReportTypeConverter extends AbstractBaseEnumConverter<ReportType, String> {
	@Override
	protected String getEnumName() {
		return "ReportType";
	}

	@Override
	protected ReportType[] getValueList() {
		return ReportType.values();
	}
}
