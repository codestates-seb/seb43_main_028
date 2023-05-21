package backend.section6mainproject.advice;

import backend.section6mainproject.exception.BusinessLogicException;
import backend.section6mainproject.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class StompExceptionAdvice {
    private final SimpMessagingTemplate messagingTemplate;

    @MessageExceptionHandler
    //필드 유효성검사 실패, 변수값 등등 요청,검증 단계 예외처리
    public void handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.of(e.getBindingResult());
        sendError(response);
    }

    @MessageExceptionHandler
    //비즈니스 로직 예외를 처리하는 메소드.
    public void handleBusinessLogicException(BusinessLogicException e) {
        ErrorResponse response = ErrorResponse.of(e.getExceptionCode());
        sendError(response);
    }

    @MessageExceptionHandler
    //JSON,XML등등 지원되지 않는 형식이 아닌경우 예외 처리
    public void handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {

        ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST,
                "Required request body is missing");
        sendError(response);
    }
    @MessageExceptionHandler
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        throw e;
    }

    @MessageExceptionHandler
    //모든 예외를 처리하는 메소드(디폴트)
    public void handleException(Exception e) {
        log.error("# handle Exception", e);
        ErrorResponse response = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR);
        sendError(response);
    }

    private void sendError(ErrorResponse errorResponse) {
        Object attr = SimpAttributesContextHolder.getAttributes().getAttribute("walkLogId");
        if(attr == null) return;
        long walkLogId = Long.parseLong(attr.toString());
        Map<String, Object> headers = new HashMap<>();
        headers.put("error", errorResponse.getStatus());
        messagingTemplate.convertAndSend("/sub/" + walkLogId, errorResponse, headers);
    }
}
