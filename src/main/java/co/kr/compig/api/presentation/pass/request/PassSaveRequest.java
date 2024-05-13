package co.kr.compig.api.presentation.pass.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassSaveRequest {

	private String memberId;

	private String encodeData;
}
