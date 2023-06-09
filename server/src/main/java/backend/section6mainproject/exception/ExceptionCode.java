package backend.section6mainproject.exception;

import lombok.Getter;

public enum ExceptionCode {
    WRONG_MONTH_INPUT(400,"Not Input Year"),
    WRONG_DAY_INPUT(400,"Month or Year Not Input"),
    WALK_LOG_ALREADY_RECORDING(400,"WalkLog already recording"),
    CANNOT_UPLOAD_WALK_LOG_CONTENT(400, "Cannot upload walkLogContent"),
    CAN_NOT_CHANGE_WALK_LOG(403,"WalkLog Can Not Change"),
    CAN_NOT_EXIT_WALK_LOG(403,"WalkLog Can Not Exit"),
    MEMBER_NOT_FOUND(404, "Member Not Found"),// 존재하지 않는 회원
    WALK_LOG_NOT_FOUND(404,"WalkLog Not Found"),//존재하지 않는 걷기 기록
    WALK_LOG_CONTENT_NOT_FOUND(404, "WalkLogContent Not Found"), //존재하지 않는 걷기 기록 컨텐츠
    MEMBER_EMAIL_EXISTS(409, "Member Email exists"),
    MEMBER_NICKNAME_EXISTS(409, "Member Nickname exists");
    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
