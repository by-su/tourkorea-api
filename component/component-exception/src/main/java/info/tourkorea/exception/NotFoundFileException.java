package info.tourkorea.exception;

import response.ErrorCode;

public class NotFoundFileException extends BaseException {
    public NotFoundFileException() {
        super(ErrorCode.FILE_NOT_FOUND);
    }
}
