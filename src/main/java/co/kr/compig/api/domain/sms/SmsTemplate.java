package co.kr.compig.api.domain.sms;

import java.util.HashSet;
import java.util.Set;

import co.kr.compig.api.presentation.sms.request.SmsTemplateUpdateRequest;
import co.kr.compig.api.presentation.sms.response.SmsTemplateResponse;
import co.kr.compig.global.code.SmsTemplateType;
import co.kr.compig.global.code.converter.SmsTemplateTypeConverter;
import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
@SequenceGenerator(
	name = "sms_template_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "sms_template_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class SmsTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sms_template_seq_gen")
	@Column(name = "sms_template_id")
	private Long id;

	@Column(length = 3, nullable = false)
	@Convert(converter = SmsTemplateTypeConverter.class)
	private SmsTemplateType smsTemplateType; //SMS 템플릿 종류

	private String templateCode; //카카오 알림톡 템플릿 코드

	@Column(columnDefinition = "TEXT")
	private String contents; //내용

  /* =================================================================
  * Default columns
  ================================================================= */

	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndUpdated = new CreatedAndUpdated();

  /*=================================================================
  * Domain mapping
  ===================================================================*/

	@OneToMany(mappedBy = "smsTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private final Set<Sms> sms = new HashSet<>();

	public SmsTemplateResponse toSmsTemplateDetailResponse() {
		return SmsTemplateResponse.builder()
			.smsTemplateId(this.id)
			.smsTemplateType(this.smsTemplateType)
			.contents(this.contents)
			.build();
	}

	public void update(SmsTemplateUpdateRequest smsTemplateUpdateRequest) {
		this.smsTemplateType = smsTemplateUpdateRequest.getSmsTemplateType();
		this.contents = smsTemplateUpdateRequest.getContents();
	}

  /*====================================================================
  * Relation method
  ======================================================================*/

}
