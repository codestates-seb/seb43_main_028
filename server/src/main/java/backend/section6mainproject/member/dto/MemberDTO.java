package backend.section6mainproject.member.dto;

import backend.section6mainproject.validator.NotSpace;
import backend.section6mainproject.walklog.dto.WalkLogDTO;
import backend.section6mainproject.walklog.entity.WalkLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class MemberDTO {
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
        private Long memberId;
        @NotSpace(message = "비밀번호를 변경해주십시오.")
        private String password;
        @NotSpace(message = "닉네임을 입력해주세요.")
        private String nickname;
        @NotSpace(message = "자기소개문구는 공백일 수 없습니다.")
        private String introduction;

        private WalkLog.WalkLogPublicSetting defaultWalkLogPublicSetting;
    }
    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long memberId;
        private String email;
        private String nickname;
        private String introduction;
        private String defaultWalkLogPublicSetting;
        private String imageUrl;
        private int totalWalkLog;
        private int totalWalkLogContent;
        //추후 프론트측에서 필요한 속성 정할 예정
        //private List<WalkLogDTO.Response> walkLogs;
    }
}