package co.kr.compig.api.presentation.check.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckNameRequest {
	@NotBlank
	private String jumin1;
	@NotBlank
	private String jumin2;
	@NotBlank
	private String name;
}
