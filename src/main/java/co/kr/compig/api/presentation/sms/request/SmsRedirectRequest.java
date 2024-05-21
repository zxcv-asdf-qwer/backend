package co.kr.compig.api.presentation.sms.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//비즈뿌리오-> 백엔드 결과전송
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SmsRedirectRequest {

	@JsonProperty("DEVICE")
	private String device; //메시지 유형

	@JsonProperty("CMSGID")
	private String cmsgid; //메시지 키

	@JsonProperty("MSGID")
	private String msgid; //비즈뿌리오 메시지 키

	@JsonProperty("PHONE")
	private String phone; //수신 번호

	@JsonProperty("MEDIA")
	private String media; //실제 발송된 메시지 상세 유형

	@JsonProperty("TO_NAME")
	private String to_name; //실제 발송된 메시지 상세 유형

	@JsonProperty("UNIXTIME")
	private String unixtime; //발송 시간

	@JsonProperty("RESULT")
	private String result; //발송 결과 코드

	@JsonProperty("USERDATA")
	private String userdata; //정산용 부서 코드

	@JsonProperty("WAPINFO")
	private String wapinfo; //이통사/카카오 정보 * SKT/KTF/LGT/KAO

	@JsonProperty("TELRES")
	private String telres; //이통사 대체 발송 결과

	@JsonProperty("TELTIME")
	private String teltime; //이통사 대체 발송 시간

	@JsonProperty("KAORES")
	private String kaores; //카카오 대체 발송 결과

	@JsonProperty("KAOTIME")
	private String kaotime; //카카오 대체 발송 시간

	@JsonProperty("RCSRES")
	private String rcsres; //RCS 대체 발송 결과

	@JsonProperty("RCSTIME")
	private String rcstime; //RCS 대체 발송 시간

	@JsonProperty("RETRY_FLAG")
	private String retry_flag; //대체 발송 정보

	@JsonProperty("RESEND_FLAG")
	private String resend_flag; //대체 발송 메시지 유형

	@JsonProperty("REFKEY")
	private String refkey; //고객사에서 부여한 키

}
