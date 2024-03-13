package co.kr.compig.global.dto.pagination.nouse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import co.kr.compig.global.dto.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class PagingResult extends BaseAudit {

	@JsonIgnore
	private int totalCount;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer rowNum;

}
