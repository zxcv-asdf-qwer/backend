package co.kr.compig.api.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HospitalDetailResponse {
  private Long hospitalId; // 병원 id
  private String hospitalNm; // 병원 명
  private String hospitalCode; // 병원 우편번호
  private String hospitalAddress1; // 병원 주소
  private String hospitalAddress2; // 병원 상세 주소
  private String hospitalTelNo; // 병원 전화번호
  private String hospitalOperationHours; // 병원 운영 시간
}
