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
//특수문자의 상한선을 확실히 정할 것 ㅇㅋ?

    @Getter
    @Setter
    public static class Post {
        @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "올바른 형식의 이메일을 입력해주세요.")
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$^#!%*?&()_.])[a-zA-Z\\d\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-_+<>@\\#$%&\\\\\\=\\(\\'\\\"]{10,15}$",message = "소문자,숫자,특수문자 각 1자 포함 총 10자 이상 15자 이하")
        private String password;

        @Pattern(regexp = "^[A-Za-z0-9가-힣]*$", message = "닉네임은 영문, 숫자, 한글만 허용됩니다.")
        @Size(min = 1, max = 16)
        @NotBlank(message = "닉네임을 입력해주세요.")
        private String nickname;

    }

    @Getter
    @Setter
    public static class Patch {
        @Pattern(regexp = "^[A-Za-z0-9가-힣]*$", message = "닉네임은 영문, 숫자, 한글만 허용됩니다.")
        @Size(min = 1, max = 16)
        @NotSpace(message = "닉네임을 입력해주세요.")
        private String nickname;

        @Size(max = 100) //자기소개 글자수 제한
        private String introduction;

        private WalkLog.WalkLogPublicSetting defaultWalkLogPublicSetting;
    }

    @Getter
    @Setter
    public static class PatchPw {
        @NotBlank(message = "재설정하실 비밀번호를 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$^#!%*?&()_.])[a-zA-Z\\d\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-_+<>@\\#$%&\\\\\\=\\(\\'\\\"]{10,15}$",message = "소문자,숫자,특수문자 각 1자 포함 총 10자 이상 15자 이하")        private String password;
    }

    @Getter
    @Setter
    public static class GetNewPw {
        @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "올바른 형식의 이메일을 입력해주세요.")
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
        private long totalWalkLog;
        private long totalWalkLogContent;
        private LocalDateTime createdAt; // 회원가입일시 공개
    }
}
