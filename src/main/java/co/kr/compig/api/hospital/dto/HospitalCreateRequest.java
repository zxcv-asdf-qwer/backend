package co.kr.compig.api.hospital.dto;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import co.kr.compig.domain.hospital.Hospital;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalCreateRequest {
	@NotBlank
	@Length(min = 2, max = 100)
	private String hospitalNm; // 병원 명
	@NotBlank
	private String hospitalCode; // 병원 우편번호
	@NotBlank
	@Length(min = 2, max = 200)
	private String hospitalAddress; // 병원 주소
	@Length(max = 100)
	private String hospitalTelNo; // 병원 전화번호
	private String hospitalOperationHours; // 병원 운영 시간

	public Hospital converterEntity() {
		return Hospital.builder()
			.hospitalNm(this.hospitalNm)
			.hospitalCode(this.hospitalCode)
			.hospitalAddress1(this.hospitalAddress)
			.hospitalTelNo(this.hospitalTelNo)
			.hospitalOperationHours(this.hospitalOperationHours)
			.build();
	}
}
