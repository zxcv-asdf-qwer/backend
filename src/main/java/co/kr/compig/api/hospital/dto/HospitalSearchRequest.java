package co.kr.compig.api.hospital.dto;

import co.kr.compig.common.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class HospitalSearchRequest extends PageableRequest {
	private String hospitalNm; // 병원명
}
