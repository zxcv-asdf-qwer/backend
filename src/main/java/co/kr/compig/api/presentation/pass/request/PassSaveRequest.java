package co.kr.compig.api.presentation.pass.request;

import jakarta.validation.constraints.NotBlank;
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
	@NotBlank
	private String name;
	@NotBlank
	private String birthdate;
	@NotBlank
	private String gender; //0 여성, 1 남성
	@NotBlank
	private String nationalInfo; //0 내국인, 1 외국인
	@NotBlank
	private String mobile;
	@NotBlank
	private String dupInfo;
	@NotBlank
	private String connInfo;
}
