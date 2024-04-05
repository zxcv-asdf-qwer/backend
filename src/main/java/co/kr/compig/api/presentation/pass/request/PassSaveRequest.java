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

	@NotBlank
	String name;
	@NotBlank
	String birthdate;
	@NotBlank
	String gender; //0 여성, 1 남성
	@NotBlank
	String nationalInfo; //0 내국인, 1 외국인
	@NotBlank
	String mobile;
	@NotBlank
	String dupInfo;
	@NotBlank
	String connInfo;
}
