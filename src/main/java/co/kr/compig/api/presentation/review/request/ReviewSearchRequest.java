package co.kr.compig.api.presentation.review.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class ReviewSearchRequest extends PageableRequest {
	@NotNull
	private Long orderId;
}
