package co.kr.compig.common.code.converter;


import co.kr.compig.common.code.BaseEnumCode;
import jakarta.persistence.AttributeConverter;
import java.util.Arrays;

public abstract class AbstractBaseEnumConverter<X extends Enum<X> & BaseEnumCode<Y>, Y> implements
    AttributeConverter<X, Y> {

  protected abstract String getEnumName();

  protected abstract X[] getValueList();

  @Override
  public Y convertToDatabaseColumn(X attribute) {
    return attribute == null ? null : attribute.getCode();
  }

  @Override
  public X convertToEntityAttribute(Y dbData) {
    return dbData == null ? null : Arrays.stream(getValueList())
        .filter(e -> e.getCode().equals(dbData))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("Enum %s에 Code %s가 없습니다.", getEnumName(), dbData)));
  }

}
