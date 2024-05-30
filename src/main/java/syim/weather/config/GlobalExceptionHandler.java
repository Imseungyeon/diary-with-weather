package syim.weather.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Exception handleAllException(){
        // 예외를 로거에 저장, 알람 보내도록 설정, DB 재시작 등의 예외 처리를 위한 로직 작성
        System.out.println("error from GlobalExceptionHandler");
        return new Exception();
    }
}
