package info.tourkorea.exception;

import response.ErrorCode;

public class AuthorizationFailException extends BaseException {

    public AuthorizationFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
