package co.kr.compig.global.embedded;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Audit {

	@Column(length = 50, updatable = false)
	private String createdBy; // 등록자 아이디

	@Column(length = 50)
	private String updatedBy; // 수정자 아이디

	@Column(updatable = false)
	private LocalDateTime createdAt; // 등록일시

	private LocalDateTime updatedAt; // 수정일시

	@PrePersist
	public void prePersist() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
		this.preUpdate();
	}

	@PreUpdate
	public void preUpdate() {
		updatedAt = LocalDateTime.now();
	}

}
