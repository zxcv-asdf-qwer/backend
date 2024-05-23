package co.kr.compig.api.domain.sms;

import java.util.HashSet;
import java.util.Set;

import co.kr.compig.api.presentation.infotemplate.request.InfoTemplateUpdateRequest;
import co.kr.compig.api.presentation.infotemplate.response.InfoTemplateResponse;
import co.kr.compig.global.code.converter.InfoTemplateTypeConverter;
import co.kr.compig.global.code.infoTemplateType;
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
	name = "info_template_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "info_template_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class InfoTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "info_template_seq_gen")
	@Column(name = "info_template_id")
	private Long id;

	@Column(length = 3, nullable = false)
	@Convert(converter = InfoTemplateTypeConverter.class)
	private infoTemplateType infoTemplateType; //SMS 템플릿 종류

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

	@OneToMany(mappedBy = "infoTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private final Set<Sms> sms = new HashSet<>();

	public InfoTemplateResponse toInfoTemplateDetailResponse() {
		return InfoTemplateResponse.builder()
			.infoTemplateId(this.id)
			.infoTemplateType(this.infoTemplateType)
			.templateCode(this.templateCode)
			.contents(this.contents)
			.build();
	}

	public void update(InfoTemplateUpdateRequest infoTemplateUpdateRequest) {
		this.infoTemplateType = infoTemplateUpdateRequest.getInfoTemplateType();
		this.templateCode = infoTemplateUpdateRequest.getTemplateCode();
		this.contents = infoTemplateUpdateRequest.getContents();
	}

  /*====================================================================
  * Relation method
  ======================================================================*/

}
