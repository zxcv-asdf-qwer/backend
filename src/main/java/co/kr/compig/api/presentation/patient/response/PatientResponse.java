package co.kr.compig.api.presentation.patient.response;

import java.time.LocalDate;

import co.kr.compig.global.code.GenderCode;
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
	private String name;    // 환자 이름
	private LocalDate birthday; // 생년월일
	private GenderCode gender; // 성별
}
