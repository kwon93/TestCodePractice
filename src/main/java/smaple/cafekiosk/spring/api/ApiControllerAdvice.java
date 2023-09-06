package smaple.cafekiosk.spring.api;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiControllerAdvice {
        
    //우리가 만든 서비스나 컨트롤러에서 예외가 발생한걸 관리해주는 핸들러
    @ExceptionHandler(BindException.class) //Validation 시 Binding 예외를 잡는 핸들러
    public ApiResponse<Object> bindException(BindException e){

        ObjectError data = e.getBindingResult().getAllErrors().get(0);
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                data.getDefaultMessage());
        
    }
}
