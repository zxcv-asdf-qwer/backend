package co.kr.compig.common.embedded;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ColumnDefault;

import co.kr.compig.common.utils.SecurityUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Data
@Embeddable
public class CreatedAndUpdated {

	/**********************************************
	 * Default columns
	 **********************************************/
	@Column(length = 50, updatable = false)
	private String createdBy; // 등록자 아이디

	@Column(length = 50)
	private String updatedBy; // 수정자 아이디

	@Column(updatable = false)
	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime createdOn; // 등록일시

	@ColumnDefault("CURRENT_TIMESTAMP")
	private LocalDateTime updatedOn; // 수정일시

	@PrePersist
	public void prePersist() {
		if (StringUtils.isBlank(createdBy)) {
			createdBy = SecurityUtil.getUserId();
		}
		if (createdOn == null) {
			createdOn = LocalDateTime.now();
		}

		this.preUpdate();
	}

	@PreUpdate
	public void preUpdate() {
		updatedBy = SecurityUtil.getUserId();
		updatedOn = LocalDateTime.now();
	}

	public CreatedAndUpdated() {
	}

	public CreatedAndUpdated(String createdBy, LocalDateTime createdOn, String updatedBy,
		LocalDateTime updatedOn) {
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
	}

	public String getCreatedOnString() {
		return this.createdOn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public String getUpdatedOnString() {
		return this.updatedOn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
}
