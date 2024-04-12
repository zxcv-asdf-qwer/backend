package co.kr.compig.api.presentation.settle.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@Setter
@SuperBuilder(toBuilder = true)
public class SettleSearchRequest extends PageableRequest {

}
