package co.kr.compig.common.dto.pagination;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Getter
public class PageResponse<T> implements Serializable {

  private List<T> data;

  private boolean hasNext;


  //기본 생성자 호출시 empty list
  public PageResponse() {
    this.data = Collections.emptyList();
    this.hasNext = false;
  }

  public PageResponse(List<T> content, Pageable pageable, Long totalCount) {
    final PageImpl<T> page = new PageImpl<>(content, pageable, totalCount);

    this.data = page.getContent();
    this.hasNext = page.hasNext();
  }

  public PageResponse(PageImpl<T> page) {
    this.data = page.getContent();
    this.hasNext = page.hasNext();
  }
}
