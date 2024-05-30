package co.kr.compig.api.presentation.payment.response;

import java.time.LocalDateTime;

import co.kr.compig.global.code.PaymentType;
import co.kr.compig.global.dto.BaseAudit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class PaymentDetailResponse extends BaseAudit {
	@Schema(description = "결제 ID")
	private Long id; // 결제 ID
	@Schema(description = "간병공고 ID")
	private Long careOrderId; // 간병공고 ID
	@Schema(description = "결제 유형")
	private PaymentType paymentType; //결제 유형
	@Schema(description = "결제url 기한")
	private LocalDateTime paymentExpireDate; // 결제url 기한
	@Schema(description = "결제일")
	private LocalDateTime paymentCompleteDate; // 결제일
	@Schema(description = "결제요청 결과")
	private String paymentRequestResult; // 결제요청 결과
	@Schema(description = "결제 결과")
	private String paymentResponseResult; // 결제 결과
	@Schema(description = "결제금액")
	private String paymentAmount; // 결제금액
	//+결제요청
}
