package co.kr.compig.api.presentation.apply.request;

import co.kr.compig.global.code.ApplyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyUpdateRequest {
	private ApplyStatus applyStatus;
}
