package co.kr.compig.common.exception;

import co.kr.compig.common.exception.dto.ErrorCode;

public class UploadException extends RuntimeException {
	ErrorCode errorCode;

	public UploadException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public UploadException(ErrorCode errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}
}
