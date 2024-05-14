package co.kr.compig.api.infrastructure.sms.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import co.kr.compig.global.config.jackson.NonEmptyListSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(Include.NON_NULL) //null 이면 포함시키지 않음
public class BizPpurioSendRequest { //sms만 발송

	private String account; //비즈뿌리오 계정
	private String type; //메세지데이터
	private String from; //발신 번호
	private String to; //수신 번호
	private String country; //국가코드, 현재 사용 안함
	private Content content; //메시지 데이터
	private String refkey; //고객사에서 부여한 키, 비즈뿌리오에 보내는 unique 값
	private String userinfo; // 정산용 부서 코드
	private String resllercode; //부가 사업자 식별코드
	private String sendtime; //발송시간 (즉시발송 원하면 미입력)
	private Resend resend; //대체 발송 메세지 유형
	private Recontent recontent; //대체 발송 메시지 데이터

	@Getter
	@Builder
	@JsonInclude(Include.NON_NULL)
	public static class Content {

		private Sms sms;
		private At at;
	}

	@Getter
	@Builder
	@JsonInclude(Include.NON_NULL)
	public static class Sms {

		private String message;
	}

	@Getter
	@Builder
	@JsonInclude(Include.NON_NULL)
	public static class At {

		private String senderkey;
		private String templatecode;
		private String message;
		@JsonSerialize(using = NonEmptyListSerializer.class)
		private List<Button> button;
	}

	@Getter
	@Builder
	@JsonInclude(Include.NON_NULL)
	public static class Button {

		private String name;
		private String type;
		@JsonProperty("url_mobile")
		private String urlMobile;
		@JsonProperty("url_pc")
		private String urlPc;
	}

	@Getter
	@Builder
	@JsonInclude(Include.NON_NULL)
	public static class Resend {

		private String first;// 1치 대체 발송 메세지 유형 *sms, mms, Ims, at, ai, res
	}

	@Getter
	@Builder
	@JsonInclude(Include.NON_NULL)
	public static class Recontent {

		private Sms sms;
	}
}
