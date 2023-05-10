package backend.section6mainproject.advice;

import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice
public class StompExceptionAdvice {
    @MessageExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //필드 유효성검사 실패, 변수값 등등 요청,검증 단계 예외처리
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e){
        final ErrorResponse response = ErrorResponse.of(e.getBindingResult());
        return response;
    }
    @MessageExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //JPA에서 제약 조건 위반 예외 처리,(ex : DB에 저장할려는데 유효성 검사 실패)
    public ErrorResponse handleConstraintViolationException(
            ConstraintViolationException e){
        final ErrorResponse response = ErrorResponse.of(e.getConstraintViolations());
        return response;
    }
    @MessageExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    //모든 예외를 처리하는 메소드(디폴트)
    public ErrorResponse handleException(Exception e){
        log.error("# handle Exception",e);
        final ErrorResponse response = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR);
        return response;
    }
    @MessageExceptionHandler
    //비즈니스 로직 예외를 처리하는 메소드.
    public ResponseEntity handleBusinessLogicException(BusinessLogicException e) {
        final ErrorResponse response = ErrorResponse.of(e.getExceptionCode());

        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getExceptionCode()
                .getStatus()));
    }
    @MessageExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    //지원되지 않는 HTTP메소드 예외를 처리하는 메소드
    public ErrorResponse handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {

        final ErrorResponse response = ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED);

        return response;
    }
    @MessageExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //JSON,XML등등 지원되지 않는 형식이 아닌경우 예외 처리
    public ErrorResponse handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {

        final ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST,
                "Required request body is missing");

        return response;
    }
    @MessageExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //요청 매개 변수중 필수 매개 변수가 누락 되었을때 예외처리
    public ErrorResponse handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {

        final ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST,
                e.getMessage());

        return response;
    }
}
