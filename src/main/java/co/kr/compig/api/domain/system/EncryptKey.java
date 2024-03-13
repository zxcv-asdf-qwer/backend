package co.kr.compig.api.domain.system;

import co.kr.compig.api.domain.code.EncryptTarget;
import co.kr.compig.api.domain.code.EncryptType;
import co.kr.compig.api.domain.code.converter.EncryptTargetConverter;
import co.kr.compig.api.domain.code.converter.EncryptTypeConverter;
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
@Table
@SequenceGenerator(
	name = "encrypt_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "encrypt_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class EncryptKey {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "encrypt_seq_gen")
	@Column(name = "encrypt_id")
	private Long id;

	@Column(nullable = false)
	@Convert(converter = EncryptTypeConverter.class)
	private EncryptType encryptType; //암호화 종류

	@Column(nullable = false)
	private String encryptKey; //암호화 키

	@Column(nullable = false)
	@Convert(converter = EncryptTargetConverter.class)
	private EncryptTarget encryptTarget; //암호화 대상

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
