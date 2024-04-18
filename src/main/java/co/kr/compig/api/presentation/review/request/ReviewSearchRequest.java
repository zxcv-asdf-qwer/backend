package co.kr.compig.api.presentation.review.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class ReviewSearchRequest extends PageableRequest {
	@NotNull
	private Long orderId;
}
