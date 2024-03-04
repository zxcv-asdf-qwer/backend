package co.kr.compig.api.settle.dto;

import co.kr.compig.domain.settle.Settle;
import co.kr.compig.domain.settle.SettleGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettleCreateRequest {
  @NotBlank
  private String element; // 요소명
  @NotBlank
  private Integer amount; // 금액

  public Settle converterEntity(SettleGroup settleGroup) {
    return Settle.builder()
        .element(this.element)
        .amount(this.amount)
        .settleGroup(settleGroup)
        .build();
  }
}
