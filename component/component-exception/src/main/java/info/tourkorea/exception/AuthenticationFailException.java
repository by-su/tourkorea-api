package info.tourkorea.exception;

import response.ErrorCode;

public class AuthenticationFailException extends BaseException {

    public AuthenticationFailException(ErrorCode errorCode) {
        super(errorCode);
    }


}
