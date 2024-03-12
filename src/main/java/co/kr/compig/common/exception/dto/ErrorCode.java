package co.kr.compig.common.exception.dto;

import org.springframework.http.HttpStatus;

//TODO clientMessage Spring-Cloud-Bus로 변경
public enum ErrorCode {
	//common
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 정보로 요청하였습니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error"),
	INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "Invalid Type Value"),
	FORBIDDEN(HttpStatus.FORBIDDEN, "액세스가 거부되었습니다."),
	PATH_VARIABLE_VALUE(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	ERROR(HttpStatus.valueOf(420), "오류가 발생하였습니다."),
	INVALID_NOT_EXIST_DATA(HttpStatus.valueOf(422), "데이터가 없습니다."),

	// file
	EXTRACT_INVALID(HttpStatus.BAD_REQUEST, "확장자를 추출할 수 없습니다."),
	FILE_DECODE_FAIL(HttpStatus.BAD_REQUEST, "파일 이름 디코딩에 실패했습니다."),
	;

	private final HttpStatus httpStatus;
	private String clientMessage;

	ErrorCode(final HttpStatus httpStatus, final String clientMessage) {
		this.httpStatus = httpStatus;
		this.clientMessage = clientMessage;
	}

	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

	public String getClientMessage() {
		return this.clientMessage;
	}

	public void setClientMessage(String clientMessage) {
		this.clientMessage = clientMessage;
	}
}
