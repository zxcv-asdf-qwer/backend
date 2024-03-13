package co.kr.compig.api.presentation.sms.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import co.kr.compig.api.domain.sms.Sms;
import co.kr.compig.global.utils.SecurityUtil;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSend {

	@NotNull
	private String senderPhoneNumber; //보내는 전화번호 from
	@NotNull
	private String receiverPhoneNumber; //받는 전화번호 to
	@NotNull
	private String contents; //내용
	@Builder.Default
	private String refkey = getNow(); //비즈뿌리오에 보내는 unique 값
	private LocalDateTime sendtime; //발송시간 미입력 즉시발송
	private String authNumber; //인증 번호

	private static String getNow() {
		// 현재 날짜와 시간을 구합니다.
		LocalDateTime now = LocalDateTime.now();
		// 날짜와 시간을 원하는 형태의 문자열로 포맷합니다.
		String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

		// 10자리 난수를 생성합니다.
		Random random = new Random();
		StringBuilder serialCode = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			int digit = random.nextInt(10); // 0에서 9 사이의 난수를 생성합니다.
			serialCode.append(digit);
		}

		// 포맷된 날짜와 시간 문자열과 난수를 결합하여 반환합니다.
		return formattedDateTime + serialCode; //'202403080731385069491909524'
	}

	public Sms toEntity() {
		return Sms.builder()
			.memberId(SecurityUtil.getMemberId() != null ? SecurityUtil.getMemberId() : "SYSTEM")
			.senderPhoneNumber(this.senderPhoneNumber)
			.receiverPhoneNumber(this.receiverPhoneNumber)
			.contents(this.contents)
			.refkey(this.refkey)
			.sendtime(this.sendtime)
			.ref1(authNumber)
			.build();
	}
}
