package co.kr.compig.common.code.converter;

import co.kr.compig.common.code.SmsType;
import jakarta.persistence.Converter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@Converter(autoApply = true)
@MappedTypes(SmsType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class SmsTypeConverter extends AbstractBaseEnumConverter<SmsType, String> {

  @Override
  protected String getEnumName() {
    return "SmsType";
  }

  @Override
  protected SmsType[] getValueList() {
    return SmsType.values();
  }
}
