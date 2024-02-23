package co.kr.compig.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContentsType implements BaseEnumCode<String> {
  ACCOUNT("ACCOUNT", "board.contents.type.account", UseYn.Y),  // 계정
  INSPECTION("INSPECTION", "board.contents.type.inspection", UseYn.N), // 점검
  EVENT("EVENT", "board.contents.type.event", UseYn.N),    // 이벤트
  ETC("ETC", "board.contents.type.etc", UseYn.Y)   // 기타
  ;


  private final String code;
  private final String desc;
  private final UseYn useYn;
}
