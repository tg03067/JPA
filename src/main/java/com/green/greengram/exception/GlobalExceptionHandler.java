package com.green.greengram.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/*
    Advice 라는 단어가 보이면 AOP 라고 인식하면 됨.
    AOP( Aspect Oriented Programming 관점지향 프로그래밍 )
    Exception 을 잡아낸다. ( 모두 or 개별 가능 )
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // 우리가 커스텀한 예외가 발생되었을 경우 캐치
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleException(CustomException e) {
        log.error("CustomException - handlerException: {}", e) ;
        return handleExceptionInternal(e.getErrorCode()) ;
    }
    // Validation 예외가 발생되었을 경우 캐치
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER, ex) ;
    }
    // 이외의 모든 예외 캐치
    @ExceptionHandler({Exception.class}) // 모든 Exception 잡아낸다
    public ResponseEntity<Object> handleException(Exception e) {
        log.error("Exception - handlerException: {}", e) ;
         return handleExceptionInternal(CommonErrorCode.INTERNAL_SERVER_ERROR) ;
    }
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode){
        return handleExceptionInternal(errorCode, null) ;
    }
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, BindException e){
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, e)) ;
    }
    private MyErrorResponse makeErrorResponse(ErrorCode errorCode, BindException e){
        return MyErrorResponse.builder()
                .statusCode(errorCode.getHttpStatus())
                .resultMsg(errorCode.getMessage())
                .resultData(errorCode.name()) // enum 의 이름
                .valids(e == null ? null : getValidationError(e)) // validation 에러 메시지를 정리하는 메소드
                .build() ;
    }
    private List<MyErrorResponse.ValidationError> getValidationError(BindException e){
        List<MyErrorResponse.ValidationError> list = new ArrayList<>();
        // List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors() ;
        for(FieldError err : e.getBindingResult().getFieldErrors() ){
            MyErrorResponse.ValidationError validationError = MyErrorResponse.ValidationError.of(err) ;
            list.add(validationError) ;
            // list.add(MyErrorResponse.ValidationError.of(err)) ;
        }
        return list ;
    }
//    private List<MyErrorResponse.ValidationError> getValidationError1(BindException e){
//        List<MyErrorResponse.ValidationError> list = new ArrayList<>();
//        e.getBindingResult().getFieldErrors().forEach(err -> {
//            MyErrorResponse.ValidationError validationError = MyErrorResponse.ValidationError.of(err) ;
//            list.add(validationError);
//        }) ;
//        return list ;
//    }
}
