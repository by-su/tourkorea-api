package info.tourkorea.exception;

import response.ErrorCode;

public class DataLengthOverException extends BaseException {

    public DataLengthOverException(ErrorCode errorCode) {
        super(errorCode);
    }
}
