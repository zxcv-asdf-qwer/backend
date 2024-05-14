package co.kr.compig.api.domain.sms;

import java.time.LocalDateTime;

import co.kr.compig.global.embedded.CreatedAndUpdated;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	name = "sms_seq_gen", //시퀀스 제너레이터 이름
	sequenceName = "sms_seq", //시퀀스 이름
	initialValue = 1, //시작값
	allocationSize = 1 //메모리를 통해 할당 할 범위 사이즈
)
public class Sms {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sms_seq_gen")
	@Column(name = "sms_id")
	private Long id;

	@Column
	private String memberId;
	@Column(length = 100)
	private String senderPhoneNumber;  //보내는 전화번호 from
	@Column(length = 100)
	private String receiverPhoneNumber; //받는 전화번호 to
	@Column(columnDefinition = "TEXT")
	private String contents; //내용
	@Column
	private String refkey; //비즈뿌리오에 보내는 unique 값
	@Column
	private LocalDateTime sendtime; //발송시간 미입력 즉시발송

	/* =================================================================
   * Default columns
	 ================================================================= */
	@Embedded
	@Builder.Default
	private CreatedAndUpdated createdAndModified = new CreatedAndUpdated();

	/* =================================================================
	 * Domain mapping
	 ================================================================= */
	@ManyToOne
	@JoinColumn(name = "sms_template_id", foreignKey = @ForeignKey(name = "fk01_sms"))
	private SmsTemplate smsTemplate;

  /* =================================================================
   * Relation method
   ================================================================= */

	public void setSmsTemplate(final SmsTemplate smsTemplate) {
		this.smsTemplate = smsTemplate;
	}
}
