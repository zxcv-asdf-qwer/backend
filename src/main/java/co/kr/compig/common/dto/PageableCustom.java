package co.kr.compig.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

@Getter
@NoArgsConstructor
public class PageableCustom {
  private boolean first;
  private boolean last;
  private boolean hasNext;
  private int totalPages;
  private Long totalElements;
  private int page;
  private int size;

  public PageableCustom(Slice slice){
    this.first = slice.isFirst();
    this.last = slice.isLast();
    this.hasNext = slice.hasNext();
    this.page = slice.getNumber() + 1;
    this.size = slice.getSize();
  }
}
