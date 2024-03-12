package co.kr.compig.common.dto.pagination.nouse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PagingResponse {

	private List<?> data;
	private int total;

	public static ResponseEntity<?> ok(List<? extends PagingResult> result) {
		int total = 0;

		if (CollectionUtils.isEmpty(result)) {
			result = new ArrayList<>();
		} else {
			total = result.get(0).getTotalCount();
			if (total == 0) {
				total = result.size();
			}
		}
		return ResponseEntity.ok(new PagingResponse(result, total));
	}

}
