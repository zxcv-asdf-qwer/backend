package co.kr.compig.global.dto.pagination;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class PagingRequest {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer page;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer size;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<String> sort;

	public Pageable pageable() {
		return PageRequest.of(
			ObjectUtils.isEmpty(page) ? 0 : page,
			ObjectUtils.isEmpty(size) ? 100 : size,
			sorted()
		);
	}

	public Pageable pageable(int pageIndex, int pageRow) {
		return PageRequest.of(pageIndex, pageRow);
	}

	private Sort sorted() {
		if (CollectionUtils.isEmpty(sort)) {
			return Sort.unsorted();
		} else {
			try {
				return Sort.by(
					sort.stream()
						.map(
							s -> {
								String[] sort = s.split(" ");
								if (sort[0].equals("created_on")
									|| sort[0].equals("updated_on")
									|| sort[0].equals("created_by")
									|| sort[0].equals("updated_by")) {
									return new Sort.Order(
										Sort.Direction.fromString(sort[1]), "created_and_modified." + sort[0]);
								} else {
									return new Sort.Order(Sort.Direction.fromString(sort[1]), sort[0]);
								}
							})
						.collect(Collectors.toList()));
			} catch (Exception e) {
				return Sort.unsorted();
			}
		}
	}
}
