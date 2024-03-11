package co.kr.compig.common.code.converter;

import co.kr.compig.common.code.SmsTemplateCode;
import jakarta.persistence.Converter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@Converter(autoApply = true)
@MappedTypes(SmsTemplateCode.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class SmsTemplateCodeConverter extends AbstractBaseEnumConverter<SmsTemplateCode, String> {

  @Override
  protected String getEnumName() {
    return "SmsTemplateType";
  }

  @Override
  protected SmsTemplateCode[] getValueList() {
    return SmsTemplateCode.values();
  }
}
