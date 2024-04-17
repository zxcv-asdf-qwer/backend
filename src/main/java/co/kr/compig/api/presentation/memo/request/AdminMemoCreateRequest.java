package co.kr.compig.api.presentation.memo.request;

import co.kr.compig.api.domain.memo.Memo;
import co.kr.compig.api.domain.order.CareOrder;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemoCreateRequest {

	@NotBlank
	private String contents; // 메모 내용

	public Memo converterEntity(CareOrder careOrder) {
		return Memo.builder()
			.contents(this.contents)
			.careOrder(careOrder)
			.build();
	}
}
