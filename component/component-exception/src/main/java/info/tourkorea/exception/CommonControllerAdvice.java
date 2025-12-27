package info.tourkorea.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import response.ApiResponse;

import java.time.format.DateTimeParseException;

@Slf4j
@RestControllerAdvice
public class CommonControllerAdvice {

    /**
     * http status: 500 AND result: FAIL
     * 시스템 예외 상황. 집중 모니터링 대상
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ApiResponse onException(Exception e) {
        log.error("500 Error : {}", e);
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage());
    }

    /**
     * http status: 200 AND result: FAIL
     * 시스템은 이슈 없고, 비즈니스 로직 처리에서 에러가 발생함
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = BaseException.class)
    public ApiResponse onBaseException(BaseException e) {
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.name(), e.getMessage());
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = DateTimeParseException.class)
    public ApiResponse onBaseException(DateTimeParseException e) {
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.name(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = DuplicateKeyException.class)
    public ApiResponse onBaseException(DuplicateKeyException e) {
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.name(), "Duplicated", e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public ApiResponse onBaseException(MaxUploadSizeExceededException e) {
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.name(), "Maximum upload size exceeded", e.getMessage());
    }
}
