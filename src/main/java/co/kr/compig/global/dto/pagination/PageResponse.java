package co.kr.compig.global.dto.pagination;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import lombok.Getter;

@Getter
public class PageResponse<T> implements Serializable {

	private final List<T> data;

	private final boolean hasNext;

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
