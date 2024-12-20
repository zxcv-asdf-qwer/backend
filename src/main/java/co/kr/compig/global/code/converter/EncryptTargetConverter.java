package co.kr.compig.global.code.converter;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import co.kr.compig.global.code.EncryptTarget;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
@MappedTypes(EncryptTarget.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class EncryptTargetConverter extends AbstractBaseEnumConverter<EncryptTarget, String> {

	@Override
	protected String getEnumName() {
		return "EncryptTarget";
	}

	@Override
	protected EncryptTarget[] getValueList() {
		return EncryptTarget.values();
	}
}
