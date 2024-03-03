package co.kr.compig.common.dto.pagination;

import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
public class SliceResponse<T> implements Serializable {

  private List<T> data;

  private Long cursorId;
  private boolean hasNext;

  //기본 생성자 호출시 empty list
  public SliceResponse() {
    this.data = Collections.emptyList();
    this.hasNext = false;
  }

  public SliceResponse(List<T> content, Pageable pageable, Long cursorId, boolean hasNext) {
    final SliceImpl<T> slice = new SliceImpl<>(content, pageable, hasNext);

    this.data = slice.getContent();
    this.cursorId = cursorId;
    this.hasNext = slice.hasNext();
  }

  public SliceResponse(SliceImpl<T> slice) {
    this.data = slice.getContent();
    this.hasNext = slice.hasNext();
  }

}

