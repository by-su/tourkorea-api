package info.tourkorea.exception;

import response.ErrorCode;

public class DuplicatedEntityException extends BaseException {

    public DuplicatedEntityException(String message) {
        super(message, ErrorCode.DUPLICATED_ENTITY);
    }
}
