package co.kr.compig.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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
