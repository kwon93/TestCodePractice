package smaple.cafekiosk.spring.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import smaple.cafekiosk.spring.api.service.product.response.ProductResponse;

@Getter
public class ApiResponse<T> {

    private HttpStatus status;
    private int code;
    private String message;
    private T data;

    public ApiResponse(HttpStatus status, String message, T data) {
        this.status = status;
        this.code = status.value();
        this.message = message;
        this.data = data;
    }


    public static <T>ApiResponse<T> of(HttpStatus status,String message, T data) {
        return new ApiResponse<>(status, message ,data);
    }

    public static <T>ApiResponse<T> of(HttpStatus status, T data) {
        return new ApiResponse<>(status,status.name(), data);
    }

    //200 응답이 자주 쓰인다면 ok 로 메서드를 만들기
    public static <T>ApiResponse<T> ok(T data) {
        return of(HttpStatus.OK,data);
    }
}
