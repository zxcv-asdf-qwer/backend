package co.kr.compig.api.presentation.review.request;

import co.kr.compig.global.dto.pagination.PageableRequest;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@Data
@SuperBuilder
public class ReportSearchRequest extends PageableRequest {
}
