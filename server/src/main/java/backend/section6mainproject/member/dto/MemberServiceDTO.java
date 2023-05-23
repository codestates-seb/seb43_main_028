package backend.section6mainproject.member.dto;

import backend.section6mainproject.validator.NotSpace;
import backend.section6mainproject.walklog.entity.WalkLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class MemberServiceDTO {
    @Getter
    @Setter
    public static class CreateInput {
        @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "올바른 형식의 이메일을 입력해주세요.")
        @NotSpace(message = "이메일을 입력해주세요.")
        private String email;

        @NotSpace(message = "비밀번호를 입력해주세요.")
        private String password;

        @NotSpace(message = "닉네임을 입력해주세요.")
        private String nickname;
    }

    @Getter
    @Setter
    public static class CreateOutput {
        private Long memberId;
    }

    @Getter
    @Setter
    public static class UpdateInput {
        private Long memberId;

        @NotSpace(message = "닉네임을 입력해주세요.")
        private String nickname;

        private String introduction;

        private WalkLog.WalkLogPublicSetting defaultWalkLogPublicSetting; // 비밀번호와 달리 공개설정은 거시적으로 멤버프로필에 들어간다고 볼 수 있어서 UpdateInput 하나로 퉁침

        private MultipartFile profileImage;
    }

    @Getter
    @Setter
    public static class UpdatePwInput {
        private Long memberId;
        @NotSpace(message = "비밀번호를 변경해주십시오.")
        private String password;
    }

    @Getter
    @Setter
    public static class FindNewPwInput {
        private String email;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    public static class Output {
        private Long memberId;
        private String email;
        private String nickname;
        private String introduction;
        private String defaultWalkLogPublicSetting;
        private String imageUrl;
        private Long recordingWalkLogId;
        private long totalWalkLog;
        private long totalWalkLogContent;
        private LocalDateTime createdAt; // 회원가입일시 공개
    }
}
