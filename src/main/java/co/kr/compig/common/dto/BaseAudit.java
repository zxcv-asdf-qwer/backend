package co.kr.compig.common.dto;

import co.kr.compig.common.domain.Audit;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
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
public class BaseAudit {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String createdBy; // 등록자 아이디
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private LocalDateTime createdOn; // 등록일시
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String updatedBy; // 수정자 아이디
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private LocalDateTime updatedOn; // 수정일시

  public void setCreatedAndUpdated(Audit audit) {
    if (audit != null) {
      this.createdBy = audit.getCreatedBy();
      this.createdOn = audit.getCreatedAt();
      this.updatedBy = audit.getUpdatedBy();
      this.updatedOn = audit.getUpdatedAt();
    }
  }
}

