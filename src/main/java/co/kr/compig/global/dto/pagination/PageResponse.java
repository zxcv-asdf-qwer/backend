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

	private final long totalCount;
	private final int totalPage;

	//기본 생성자 호출시 empty list
	public PageResponse() {
		this.data = Collections.emptyList();
		this.totalCount = 0L;
		this.totalPage = 0;
	}

	public PageResponse(List<T> content, Pageable pageable, Long totalCount) {
		final PageImpl<T> page = new PageImpl<>(content, pageable, totalCount);

		this.data = page.getContent();
		this.totalCount = page.getTotalElements();
		this.totalPage = page.getTotalPages();
	}

	public PageResponse(PageImpl<T> page) {
		this.data = page.getContent();
		this.totalCount = page.getTotalElements();
		this.totalPage = page.getTotalPages();
	}
}
