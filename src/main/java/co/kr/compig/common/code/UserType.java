package co.kr.compig.common.code;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserType implements BaseEnumCode<String> {

  // length = 3
  SYSTEM("SYS", "user.type.sys"), // 시스템사용자 (System)
  INTERNAL("INT", "user.type.int"), // 내부사용자 (Internal)
  GUARDIAN("GUR", "user.type.shp"), // 보호자
  PARTNER("PAR", "user.type.par"),  // 간병인
  ;

  private final String code;
  private final String desc;

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getDesc() {
    return desc;
  }
}
