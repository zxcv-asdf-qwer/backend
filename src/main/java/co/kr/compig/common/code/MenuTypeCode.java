package co.kr.compig.common.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MenuTypeCode implements BaseEnumCode<String> {
  MENU("MENU", "menu.type.menu"),
  DETAIL("DETAIL", "menu.type.detail"),
  POPUP("POPUP", "menu.type.popup");


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
