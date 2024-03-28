package co.kr.compig.api.presentation.apply.request;

import co.kr.compig.api.domain.code.ApplyStatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyUpdateRequest {
	private ApplyStatusCode applyStatusCode;
}
