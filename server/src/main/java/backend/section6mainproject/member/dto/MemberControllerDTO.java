package backend.section6mainproject.member.dto;

import backend.section6mainproject.member.entity.Member;
import backend.section6mainproject.validator.NotSpace;
import backend.section6mainproject.walklog.entity.WalkLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class MemberControllerDTO {


    @Getter
    @Setter
    public static class Post {
        @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "올바른 형식의 이메일을 입력해주세요.")
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$^#!%*?&])[a-zA-Z\\d@$^#!%*?&]{10,}$",message = "소문자,숫자,특수문자 각 1자 포함 총 10자 이상")
        private String password;

        @NotBlank(message = "닉네임을 입력해주세요.")
        private String nickname;

    }

    @Getter
    @Setter
    public static class Patch {
        @NotSpace(message = "닉네임을 입력해주세요.")
        private String nickname;

        @NotSpace(message = "자기소개문구는 공백일 수 없습니다.")
        @Size(min = 1, max = 100) //자기소개 글자수 제한
        private String introduction;

        private WalkLog.WalkLogPublicSetting defaultWalkLogPublicSetting;
    }

    @Getter
    @Setter
    public static class PatchPw {
        @NotBlank(message = "재설정하실 비밀번호를 입력해주세요.")
        private String password;
    }

    @Data
    public static class GetNewPw {
        @NotBlank(message = "회원님의 이메일을 입력해주세요.")
        private String email;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class PostResponse {
        private Long memberId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {
        private Long memberId;
        private String email;
        private String nickname;
        private String introduction;
        private WalkLog.WalkLogPublicSetting defaultWalkLogPublicSetting;
        private String imageUrl;
        private int totalWalkLog;
        private int totalWalkLogContent;
        private LocalDateTime createdAt; // 회원가입일시 공개
    }
}
