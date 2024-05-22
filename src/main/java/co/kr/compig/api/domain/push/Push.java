package co.kr.compig.api.domain.push;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.presentation.push.response.PushResponse;
import co.kr.compig.global.embedded.Created;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
@SequenceGenerator(
	name = "push_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "push_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Push {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "push_seq_gen")
	@Column(name = "push_id")
	private Long id; // Push id

	@Column(name = "title", nullable = false)
	private String title; // 메시지 제목

	@Column(name = "message", length = 500)
	private String message; // 메시지 본문

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "json", name = "recipients", nullable = false)
	private List<String> recipients; // JSON 형태의 수신자 목록

	@Column(name = "send_time")
	private LocalDateTime sendTime; // 예약된 발송 시간 또는 실제 발송 시간

	@Column(length = 250, nullable = false)
	private String deviceUuid;
	/* =================================================================
	 * Domain mapping
	   ================================================================= */

	/* =================================================================
		 * Relation method
		================================================================= */

	/* =================================================================
	 * Default columns
	 ================================================================= */
	@Embedded
	@Builder.Default
	private Created created = new Created();

	/* =================================================================
	 * Business login
	 ================================================================= */

	public void setDefaultCreated() {
		this.created = new Created(Member.builder().id("PUSH_SERVICE").build(), LocalDateTime.now());
	}

	public PushResponse toPushResponse() {
		PushResponse pushResponse = PushResponse.builder()
			.pushId(this.id)
			.message(this.message)
			.build();
		pushResponse.setCreated(this.created);
		return pushResponse;
	}
}
