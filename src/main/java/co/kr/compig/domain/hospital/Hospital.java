package co.kr.compig.domain.hospital;

import co.kr.compig.api.hospital.dto.HospitalDetailResponse;
import co.kr.compig.api.hospital.dto.HospitalUpdateRequest;
import co.kr.compig.common.embedded.CreatedAndUpdated;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
@SequenceGenerator(
    name = "hospital_seq_gen", //시퀀스 제너레이터 이름
    sequenceName = "hospital_seq", //시퀀스 이름
    initialValue = 1, //시작값
    allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Hospital {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hospital_seq_gen")
  @Column(name = "hospital_id")
  private Long id;

  @Column(length = 100)
  private String hospitalNm; // 병원 명

  @Column(length = 10)
  private String hospitalCode; // 병원 우편번호

  @Column(length = 200)
  private String hospitalAddress; // 병원 주소

  @Column(length = 100)
  private String hospitalTelNo; // 병원 전화번호

  @Column
  private String hospitalOperationHours; // 병원 운영 시간

  /* =================================================================
  * Domain mapping
  ================================================================= */

  /* =================================================================
  * Relation method
  ================================================================= */
  public void update(HospitalUpdateRequest hospitalUpdateRequest) {
    this.hospitalNm = hospitalUpdateRequest.getHospitalNm();
    this.hospitalCode = hospitalUpdateRequest.getHospitalCode();
    this.hospitalAddress = hospitalUpdateRequest.getHospitalAddress();
    this.hospitalTelNo = hospitalUpdateRequest.getHospitalTelNo();
    this.hospitalOperationHours = hospitalUpdateRequest.getHospitalOperationHours();
  }

  public HospitalDetailResponse toHospitalDetailResponse(){
    return HospitalDetailResponse.builder()
        .hospitalId(this.id)
        .hospitalNm(this.hospitalNm)
        .hospitalCode(this.hospitalCode)
        .hospitalAddress(this.hospitalAddress)
        .hospitalTelNo(this.hospitalTelNo)
        .hospitalOperationHours(this.hospitalOperationHours)
        .build();
  }
  /* =================================================================
  * Default columns
  ================================================================= */
  @Embedded
  @Builder.Default
  private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

}
