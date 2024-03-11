package co.kr.compig.common.code.converter;

import co.kr.compig.common.code.SmsTemplateType;
import jakarta.persistence.Converter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@Converter(autoApply = true)
@MappedTypes(SmsTemplateType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class SmsTemplateTypeConverter extends AbstractBaseEnumConverter<SmsTemplateType, String> {

  @Override
  protected String getEnumName() {
    return "SmsTemplateType";
  }

  @Override
  protected SmsTemplateType[] getValueList() {
    return SmsTemplateType.values();
  }
}
