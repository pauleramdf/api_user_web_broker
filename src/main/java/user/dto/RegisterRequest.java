package user.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String email;
    @NotBlank(message = "{name.not.blank}")
    private String username;
    @NotBlank(message = "{senha.not.blank}")
    private String password;
    @Min(0)
    @NotNull
    private Double dollarBalance;
}
