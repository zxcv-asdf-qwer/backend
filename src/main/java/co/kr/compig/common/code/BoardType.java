package co.kr.compig.common.code;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardType implements BaseEnumCode<String> {
  NOTICE("NOT", "desc.board.notice"); // 공지사항

  private final String code;
  private final String desc;
}
