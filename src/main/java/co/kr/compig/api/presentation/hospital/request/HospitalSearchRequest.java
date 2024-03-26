package co.kr.compig.api.presentation.hospital.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class HospitalSearchRequest extends PageableRequest {
	private String hospitalNm; // 병원명
}
