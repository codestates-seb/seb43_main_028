package backend.section6mainproject.member.dto;

import backend.section6mainproject.validator.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class  MemberDto {
    @Getter
    @Setter
    public static class Post {
        @Email
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;

        @NotBlank(message = "닉네임을 입력해주세요.")
        private String nickname;

    }
    @Getter
    @Setter
    public static class Patch {

        @NotSpace(message = "자기소개문구는 공백일 수 없습니다.")
        private String introduction;
    }
    @Getter
    @AllArgsConstructor
    public static class Response {

    }
}
