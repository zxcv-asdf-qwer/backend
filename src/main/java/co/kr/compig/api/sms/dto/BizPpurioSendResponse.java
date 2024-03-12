package co.kr.compig.api.sms.dto;

import lombok.Getter;

@Getter
public class BizPpurioSendResponse {

	private String code; //결과 코드 * API 응답 상태 및 결과 코드 참조
	private String description; //결과 메시지
	private String refkey; //메시지 키* 고객 문의 및 리포트 재 요청 기준 키
	private String messagekey; //고객사에서 부여한 키
}
