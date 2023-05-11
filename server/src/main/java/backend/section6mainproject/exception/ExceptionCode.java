package backend.section6mainproject.exception;

import lombok.Getter;

public enum ExceptionCode {
    WALK_LOG_NOT_RECORDING(400, "Not Recording Now"), //걷기 기록중이 아닙니다.
    WALK_LOG_NOT_STOP(400, "Recording Now"), //걷기 기록중입니다.
    MEMBER_NOT_FOUND(404, "Member Not Found"),// 존재하지 않는 회원
    WALK_LOG_NOT_FOUND(404,"WalkLog Not Found"),//존재하지 않는 걷기 기록
    WALK_LOG_CONTENT_NOT_FOUND(404, "WalkLogContent Not Found"), //존재하지 않는 걷기 기록 컨텐츠
    MEMBER_EXISTS(409, "Member exists");

    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
