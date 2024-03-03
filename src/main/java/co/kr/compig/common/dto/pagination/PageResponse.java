package co.kr.compig.common.dto.pagination;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Getter
public class PageResponse<T> implements Serializable {

  private List<T> items;

  private boolean hasNext;

  private Integer totalPages;

  private Long totalElements;

  private Integer page;

  private Integer size;

  private Boolean isFirst;

  private Boolean isLast;

  //기본 생성자 호출시 empty list
  public PageResponse() {
    this.items = Collections.emptyList();
    this.hasNext = false;
    this.totalPages = 0;
    this.totalElements = 0L;
    this.page = 1;
    this.size = 0;
    this.isFirst = false;
    this.isLast = false;
  }

  public PageResponse(List<T> content, Pageable pageable, Long totalCount) {
    final PageImpl<T> page = new PageImpl<>(content, pageable, totalCount);

    this.items = page.getContent();
    this.hasNext = page.hasNext();
    this.totalPages = page.getTotalPages();
    this.totalElements = page.getTotalElements();
    this.page = page.getNumber() + 1;
    this.size = page.getSize();
    this.isFirst = page.isFirst();
    this.isLast = page.isLast();
  }

  public PageResponse(PageImpl<T> page) {
    this.items = page.getContent();
    this.hasNext = page.hasNext();
    this.totalPages = page.getTotalPages();
    this.totalElements = page.getTotalElements();
    this.page = page.getNumber() + 1;
    this.size = page.getSize();
    this.isFirst = page.isFirst();
    this.isLast = page.isLast();
  }
}
