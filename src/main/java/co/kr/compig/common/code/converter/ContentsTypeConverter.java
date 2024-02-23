package co.kr.compig.common.code.converter;

import co.kr.compig.common.code.BoardType;
import co.kr.compig.common.code.ContentsType;
import jakarta.persistence.Converter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@Converter(autoApply = true)
@MappedTypes(BoardType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class ContentsTypeConverter extends AbstractBaseEnumConverter<ContentsType, String> {

  @Override
  protected String getEnumName() {
    return "ContentsType";
  }

  @Override
  protected ContentsType[] getValueList() {
    return ContentsType.values();
  }
}
