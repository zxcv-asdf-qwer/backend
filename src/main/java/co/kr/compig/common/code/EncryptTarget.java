package co.kr.compig.common.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EncryptTarget implements BaseEnumCode<String> {
  ACCOUNT("ACCOUNT", "계좌번호");


  private final String code;
  private final String desc;

  @Override
  public String getCode() {
    return null;
  }

  @Override
  public String getDesc() {
    return null;
  }
}
