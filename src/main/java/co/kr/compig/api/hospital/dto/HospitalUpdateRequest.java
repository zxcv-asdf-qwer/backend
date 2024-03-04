package co.kr.compig.api.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HospitalUpdateRequest {
  private String hospitalNm; // 병원 명
  private String hospitalCode; // 병원 우편번호
  private String hospitalAddress1; // 병원 주소
  private String hospitalAddress2; // 병원 상세 주소
  private String hospitalTelNo; // 병원 전화번호
  private String hospitalOperationHours; // 병원 운영 시간
}
