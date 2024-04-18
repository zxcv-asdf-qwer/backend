package co.kr.compig.api.presentation.review.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@Setter
@SuperBuilder(toBuilder = true)
public class ReviewSearchRequest extends PageableRequest {
	@NotNull
	private Long orderId;
}
