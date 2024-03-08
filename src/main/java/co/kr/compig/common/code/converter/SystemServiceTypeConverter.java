package co.kr.compig.common.code.converter;

import co.kr.compig.common.code.SystemServiceType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SystemServiceTypeConverter extends
    AbstractBaseEnumConverter<SystemServiceType, String> {

  @Override
  protected String getEnumName() {
    return "SystemServiceType";
  }

  @Override
  protected SystemServiceType[] getValueList() {
    return SystemServiceType.values();
  }
}
