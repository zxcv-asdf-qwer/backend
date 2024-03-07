package co.kr.compig.common.embedded;

import co.kr.compig.common.utils.SecurityUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ColumnDefault;

@Data
@Embeddable
public class Created {
  /**********************************************
   * Default columns
   **********************************************/
  @Column(length = 50, updatable = false)
  private String createdBy; // 등록자 아이디

  @Column(updatable = false)
  @ColumnDefault("CURRENT_TIMESTAMP")
  private LocalDateTime createdOn; // 등록일시

  @PrePersist
  public void prePersist() {
    if (StringUtils.isBlank(createdBy)) {
      createdBy = SecurityUtil.getUserId();
    }
    if (createdOn == null) {
      createdOn = LocalDateTime.now();
    }
  }

  public Created() {
  }

  public Created(String createdBy, LocalDateTime createdOn) {
    this.createdBy = createdBy;
    this.createdOn = createdOn;
  }

  public String getCreatedOnString() {
    return this.createdOn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }
}