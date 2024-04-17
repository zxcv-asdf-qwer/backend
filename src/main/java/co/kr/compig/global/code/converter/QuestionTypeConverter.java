package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.QuestionType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(QuestionType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class QuestionTypeConverter extends AbstractBaseEnumConverter<QuestionType, String> {
	@Override
	protected String getEnumName() {
		return "QuestionType";
	}

	@Override
	protected QuestionType[] getValueList() {
		return QuestionType.values();
	}
}
