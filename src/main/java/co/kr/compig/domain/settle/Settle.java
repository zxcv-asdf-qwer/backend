package co.kr.compig.domain.settle;

import jakarta.persistence.Column;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
@SequenceGenerator(
    name = "settle_seq_gen", //시퀀스 제너레이터 이름
    sequenceName = "settle_seq", //시퀀스 이름
    initialValue = 1, //시작값
    allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Settle {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settle_seq_gen")
  @Column(name = "settle_id")
  private Long id;



  /* =================================================================
   * Domain mapping
     ================================================================= */

}