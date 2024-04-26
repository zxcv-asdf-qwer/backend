package co.kr.compig.api.presentation.member.request;

import co.kr.compig.global.code.UseYn;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUseYnUpdate {
	@NotNull
	private UseYn useYn;
}
