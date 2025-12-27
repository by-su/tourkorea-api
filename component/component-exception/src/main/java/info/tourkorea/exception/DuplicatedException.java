package info.tourkorea.exception;

import response.ErrorCode;

public class DuplicatedException extends BaseException {

    public DuplicatedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
