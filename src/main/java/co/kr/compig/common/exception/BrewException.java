package co.kr.compig.common.exception;


import co.kr.compig.common.exception.dto.ErrorCode;

public class BrewException extends RuntimeException {

    private final ErrorCode errorCode;

    public BrewException(String clientMessage) {
        super(clientMessage);
        this.errorCode = ErrorCode.ERROR;
        this.errorCode.setClientMessage(clientMessage);
    }

    public BrewException(String clientMessage, Throwable cause) {
        super(clientMessage, cause);
        this.errorCode = ErrorCode.ERROR;
        this.errorCode.setClientMessage(clientMessage);
    }

    public BrewException(ErrorCode ee, Throwable cause) {
        super(ee.getClientMessage(), cause);
        this.errorCode = ee;
    }

    public BrewException(ErrorCode errorCode) {
        super(errorCode.getClientMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}