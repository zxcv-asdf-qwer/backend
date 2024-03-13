package co.kr.compig.api.presentation.hospital.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HospitalUpdateRequest {
	private String hospitalNm; // 병원 명
	private String hospitalCode; // 병원 우편번호
	private String hospitalAddress; // 병원 주소
	private String hospitalTelNo; // 병원 전화번호
	private String hospitalOperationHours; // 병원 운영 시간
}
