package co.kr.compig.api.presentation.patient.response;

import co.kr.compig.global.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class PatientResponse extends BaseAudit {
	private Long id; // 환자ID
	private String patientNm;    // 환자 이름
}
