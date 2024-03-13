package co.kr.compig.api.domain.app;

import org.hibernate.annotations.ColumnDefault;

import co.kr.compig.api.domain.code.AppOsType;
import co.kr.compig.api.domain.code.IsYn;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
	uniqueConstraints = {
		@UniqueConstraint(
			name = "uk01_app_version",
			columnNames = {"osCode", "lastVer", "minVer"})
	})
@SequenceGenerator(
	name = "app_version_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "app_version_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class AppVersion { // 앱 버전 체크
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_version_seq_gen")
	@Column(name = "app_version_id")
	private Long id;

	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private AppOsType osCode;        // 요청된 디바이스 os이름

	@Column(nullable = false)
	private Integer lastVer;    // 앱 사용 가능한 최신 버전 정보

	@Column(length = 50, nullable = false)
	private String lastVerNm;    // 앱 사용 가능한 최신 버전 이름

	@Column(nullable = false)
	private Integer minVer;        // 앱 사용 가능한 최소 버전 정보

	@Column(length = 50, nullable = false)
	private String minVerNm;    // 앱 사용 가능한 최소 버전 이름

	@Column(length = 1)
	@ColumnDefault("'N'")
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private IsYn forceUpdate = IsYn.N; // 강제 업데이트 여부

	/* =================================================================
	 * Default columns
	 ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

  /* =================================================================
   * Business login
   ================================================================= */

}
