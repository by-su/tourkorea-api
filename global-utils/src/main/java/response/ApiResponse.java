package response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class ApiResponse<T> {
    private String status;
    private Boolean success;
    private T data;
    private String message;

    private ApiResponse(String status, Boolean success, T data, String message) {
        this.status = status;
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(String status, T data, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .success(true)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> success(String status, T data) {
        return success(status, data, null);
    }


    public static ApiResponse<?> fail(ErrorCode errorCode) {
        return ApiResponse.builder()
                .status(errorCode.getStatus())
                .success(false)
                .message(errorCode.getErrorMsg())
                .build();
    }

    public static ApiResponse<?> fail(String status, String message) {
        return ApiResponse.builder()
                .status(status)
                .success(false)
                .message(message)
                .build();
    }

    public static ApiResponse<?> fail(String status, String message, String detail) {
        return ApiResponse.builder()
                .status(status)
                .success(false)
                .data(detail)
                .message(message)
                .build();
    }
}

