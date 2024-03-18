package co.kr.compig.api.domain.system;

import java.time.LocalDateTime;

import co.kr.compig.api.domain.code.SystemServiceType;
import co.kr.compig.api.domain.code.converter.SystemServiceTypeConverter;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "access_key")
@SequenceGenerator(
	name = "access_key_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "access_key_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class AccessKey {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "access_key_seq_gen")
	@Column(name = "access_key_id")
	private Long id;

	@Column(length = 10, nullable = false)
	@Convert(converter = SystemServiceTypeConverter.class)
	private SystemServiceType systemServiceType; //서비스 타입

	@Column(nullable = false)
	private String serviceName; //서비스 업체 명

	@Column(columnDefinition = "TEXT")
	private String accessKey; //서비스 업체 키

	@Column
	private LocalDateTime expired; //토큰 만료 시점

	/* =================================================================
	 * Default columns
	 ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

  /* =================================================================
   * Business
   ================================================================= */
}
