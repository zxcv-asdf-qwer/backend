package co.kr.compig.api.domain.app;

import co.kr.compig.api.presentation.app.request.AppVersionUpdateRequest;
import co.kr.compig.api.presentation.app.response.AppVersionResponse;
import co.kr.compig.global.code.AppOsType;
import co.kr.compig.global.code.converter.AppOsTypeConverter;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
	uniqueConstraints = {
		@UniqueConstraint(
			name = "uk01_app_version",
			columnNames = {"osCode", "minVer"})
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
	@Convert(converter = AppOsTypeConverter.class)
	private AppOsType osCode; // 요청된 디바이스 os 유형

	@Column(nullable = false)
	private String minVer; // 앱 사용 가능한 최소 버전 정보

	@Column(nullable = false)
	private String updateUrl; // 업데이트 URL

	@Column
	private String contents; // 내용

	/* =================================================================
	 * Default columns
	 ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

	public AppVersionResponse toResponse() {
		AppVersionResponse appVersionResponse = AppVersionResponse.builder()
			.appVersionId(this.id)
			.osCode(this.osCode)
			.minVer(this.minVer)
			.updateUrl(this.updateUrl)
			.build();

		appVersionResponse.setCreatedAndUpdated(this.createdAndModified);
		return appVersionResponse;
	}

	public void update(AppVersionUpdateRequest request) {
		this.minVer = request.getMinVer();
		this.updateUrl = request.getUpdateUrl();
		this.contents = request.getContents();
	}

	/* =================================================================
	 * Business login
     ================================================================= */

}


