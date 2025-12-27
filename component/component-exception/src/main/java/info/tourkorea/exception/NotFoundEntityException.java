package info.tourkorea.exception;

import response.ErrorCode;

public class NotFoundEntityException extends BaseException {

    public NotFoundEntityException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
