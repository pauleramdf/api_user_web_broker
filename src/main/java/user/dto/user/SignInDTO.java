package user.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignInDTO {
    @NotBlank(message = "{name.not.blank}")
    private String username;
    @NotBlank(message = "{senha.not.blank}")
    private String password;
}
