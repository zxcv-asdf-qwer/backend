package co.kr.compig.common.code.converter;

import co.kr.compig.common.code.BankCode;
import co.kr.compig.common.code.BoardType;
import jakarta.persistence.Converter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@Converter(autoApply = true)
@MappedTypes(BoardType.class)
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class BankCodeConverter extends AbstractBaseEnumConverter<BankCode, String>{

  @Override
  protected String getEnumName() {
    return "BankCode";
  }

  @Override
  protected BankCode[] getValueList() {
    return BankCode.values();
  }
}
