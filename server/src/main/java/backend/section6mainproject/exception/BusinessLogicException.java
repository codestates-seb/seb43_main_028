package backend.section6mainproject.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Getter
public class BusinessLogicException extends RuntimeException {
    private ExceptionCode exceptionCode;
    private Map<String, Object> attributes = new HashMap<>();

    public BusinessLogicException(ExceptionCode exceptionCode) {
        this(exceptionCode, null);
    }

    public BusinessLogicException(ExceptionCode exceptionCode, Map<String, Object> attributes) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
        Optional.ofNullable(attributes).ifPresent(attr -> this.attributes.putAll(attr));
        log.info("exception code : {}, attributes : {}", this.exceptionCode, this.attributes);
    }
}
