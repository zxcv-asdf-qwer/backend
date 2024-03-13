package co.kr.compig.global.error.exception;

import co.kr.compig.global.error.model.ErrorCode;

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
