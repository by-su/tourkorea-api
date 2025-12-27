package info.tourkorea.exception;

import response.ErrorCode;

public class FileUploadException extends BaseException {

    public FileUploadException(ErrorCode errorCode) {
        super(errorCode);
    }
}
