package co.kr.compig.common.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MenuDivCode implements BaseEnumCode<String> {

  ROOT("ROOT", "menu.div.root"),
  ADMIN("ADMIN", "menu.div.admin"),
  WMS("WMS", "menu.div.wms"),
  OMS("OMS", "menu.div.oms"),
  CARRIER("CARRIER", "menu.div.carrier"),
  SETTLE("SETTLE", "menu.div.settle"),
  CUSTOMS("CUSTOMS", "menu.div.customs");

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
