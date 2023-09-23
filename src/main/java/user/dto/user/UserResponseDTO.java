package user.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import user.model.User;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDTO {
    private String username;
    private Double dollarBalance;
    private boolean enabled;
    private Timestamp created;
    private Timestamp updated;

    public UserResponseDTO(User user) {
        this.username = user.getUsername();
        this.dollarBalance = user.getDollarBalance();
        this.enabled = user.isEnabled();
        this.created = user.getCreated();
        this.updated = user.getUpdated();
    }
}
