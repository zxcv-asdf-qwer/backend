package co.kr.compig.global.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import co.kr.compig.global.dto.pagination.PagingResult;
import co.kr.compig.global.embedded.Created;
import co.kr.compig.global.embedded.CreatedAndUpdated;
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
public class BaseAudit extends PagingResult {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String createdBy; // 등록자 아이디
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDateTime createdOn; // 등록일시
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String updatedBy; // 수정자 아이디
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private LocalDateTime updatedOn; // 수정일시

	public void setCreatedAndUpdated(CreatedAndUpdated createdAndUpdated) {
		if (createdAndUpdated != null) {
			this.createdBy = createdAndUpdated.getCreatedBy();
			this.createdOn = createdAndUpdated.getCreatedOn();
			this.updatedBy = createdAndUpdated.getUpdatedBy();
			this.updatedOn = createdAndUpdated.getUpdatedOn();
		}
	}

	public void setCreated(Created created) {
		if (created != null) {
			this.createdBy = created.getCreatedBy();
			this.createdOn = created.getCreatedOn();
		}
	}
}

