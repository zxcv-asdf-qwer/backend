package co.kr.compig.api.domain.log;

import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 로그 엔티티
 */
@Getter
@NoArgsConstructor
@Entity
@SuperBuilder(toBuilder = true)
@SequenceGenerator(
	name = "api_log_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "api_log_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class ApiLog {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "api_log_seq_gen")
	@Column(name = "log_id")
	private Long id;

	@Column
	private String userId;

	@Column(length = 10)
	private String httpMethod;

	@Column(length = 500)
	private String requestUrl;

	@Column(name = "ip_addr", length = 100)
	private String remoteIp;

	/* =================================================================
	 * Default columns
	   ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();
}
