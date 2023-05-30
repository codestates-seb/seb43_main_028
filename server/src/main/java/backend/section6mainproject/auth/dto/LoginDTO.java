package backend.section6mainproject.auth.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private boolean autoLogin;
}
