package co.kr.compig.api.domain.push;

import java.time.LocalDate;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import co.kr.compig.api.domain.member.Member;
import co.kr.compig.api.presentation.push.response.DeviceResponse;
import co.kr.compig.global.code.IsYn;
import co.kr.compig.global.code.PhoneTypeCode;
import co.kr.compig.global.embedded.Created;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
			name = "uk01_device",
			columnNames = {"deviceUuid"})
	})
public class Device {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "device_id")
	private Long id;

	@Column(length = 1, nullable = false)
	@Enumerated(EnumType.STRING)
	private PhoneTypeCode phoneType;

	@Column(length = 250, nullable = false)
	private String deviceUuid;

	@Column(length = 250)
	private String pushKey;

	@Column(length = 25)
	private String modelName;

	@Column(length = 15)
	private String osVersion;

	@Column(length = 1)
	@Enumerated(EnumType.STRING)
	private IsYn isAgreeReceive;

	private LocalDate agreeReceiveDate;

	/* =================================================================
	* Domain mapping
	================================================================= */

	// Member id
	@JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk01_member"))
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	/* =================================================================
		 * Relation method
		================================================================= */
	public void setMember(Member member) {
		this.member = member;
	}

	/* =================================================================
	 * Default columns
	 ================================================================= */
	@Embedded
	@Builder.Default
	private Created created = new Created();

	/* =================================================================
	 * Business login
	 ================================================================= */
	public void update(PhoneTypeCode phoneType, String pushKey, String modelName, String osVersion,
		IsYn isAgreeReceive) {
		if (ObjectUtils.isNotEmpty(phoneType)) {
			this.phoneType = phoneType;
		}
		if (StringUtils.isNotBlank(pushKey)) {
			this.pushKey = pushKey;
		}
		if (StringUtils.isNotBlank(modelName)) {
			this.modelName = modelName;
		}
		if (StringUtils.isNotBlank(osVersion)) {
			this.osVersion = osVersion;
		}
		if (ObjectUtils.isNotEmpty(isAgreeReceive)) {
			this.isAgreeReceive = isAgreeReceive;
			this.agreeReceiveDate = LocalDate.now();
		}
	}

	public DeviceResponse converterDto() {
		return DeviceResponse.builder()
			.deviceId(this.id)
			.userId(this.member.getUserId())
			.phoneType(this.phoneType)
			.deviceUuid(this.deviceUuid)
			.pushKey(this.pushKey)
			.modelName(this.modelName)
			.osVersion(this.osVersion)
			.agreeReceiveDate(this.agreeReceiveDate)
			.isAgreeReceive(this.isAgreeReceive)
			.build();
	}

}
