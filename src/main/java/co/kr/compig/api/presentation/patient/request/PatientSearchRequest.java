package co.kr.compig.api.presentation.patient.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class PatientSearchRequest extends PageableRequest {

	private String memberId; // 멤버 ID
}
