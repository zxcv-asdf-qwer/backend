package co.kr.compig.api.account.dto;

import co.kr.compig.common.code.BankResponseCode;
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
public class AccountCheckResponse {
  private BankResponseCode bankResponseCode;
}
