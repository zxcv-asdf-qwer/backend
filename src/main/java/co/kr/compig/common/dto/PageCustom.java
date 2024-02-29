package co.kr.compig.common.dto;


import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

@Getter
public class PageCustom<T> implements Serializable {
  private List<T> data;
  private PageableCustom pageableCustom;

  public PageCustom(List<T> content, Pageable pageable, boolean hasNext){
    this.data = content;
    this.pageableCustom = new PageableCustom(new SliceImpl(content, pageable, hasNext));
  }
}
