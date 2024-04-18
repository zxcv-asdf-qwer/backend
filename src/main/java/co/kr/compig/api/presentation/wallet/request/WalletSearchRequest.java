package co.kr.compig.api.presentation.wallet.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class WalletSearchRequest extends PageableRequest {
	private String userNm;
}
