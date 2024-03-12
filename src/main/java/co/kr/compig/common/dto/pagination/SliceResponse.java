package co.kr.compig.common.dto.pagination;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import lombok.Getter;

@Getter
public class SliceResponse<T> implements Serializable {

	private final List<T> data;

	private final boolean hasNext;

	//기본 생성자 호출시 empty list
	public SliceResponse() {
		this.data = Collections.emptyList();
		this.hasNext = false;
	}

	public SliceResponse(List<T> content, Pageable pageable, boolean hasNext) {
		final SliceImpl<T> slice = new SliceImpl<>(content, pageable, hasNext);

		this.data = slice.getContent();
		this.hasNext = slice.hasNext();
	}

	public SliceResponse(SliceImpl<T> slice) {
		this.data = slice.getContent();
		this.hasNext = slice.hasNext();
	}

}

