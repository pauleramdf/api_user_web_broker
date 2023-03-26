package user.dto.user;


import lombok.Data;
import user.model.User;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
public class CreateUserDTO {
    @NotBlank(message = "{name.not.blank}")
    private String username;
    @NotBlank(message = "{senha.not.blank}")
    private String password;
    @Min(0)
    @NotNull
    private Double dollarBalance;

    public User transformaDTO(){
        if(Objects.isNull(dollarBalance)){
            dollarBalance = 1000D;
        }
        return new User(username, password, dollarBalance);
    }
}

