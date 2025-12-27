package info.tourkorea.exception;

import response.ErrorCode;

public class PasswordNotMatchException extends BaseException {
    public PasswordNotMatchException() {
        super(ErrorCode.PASSWORD_NOT_MATCH);
    }
}

