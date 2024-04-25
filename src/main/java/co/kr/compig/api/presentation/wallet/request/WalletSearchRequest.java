package co.kr.compig.api.presentation.wallet.request;

import co.kr.compig.global.code.ExchangeType;
import co.kr.compig.global.dto.pagination.PageableRequest;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class WalletSearchRequest extends PageableRequest {
	private String userNm;
	@Parameter(description = "수기 정산 내역 = HAND")
	private ExchangeType exchangeType; //수기, 자동
}
