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
    private Long id;
    private String username;
    private Double dollarBalance;
    private boolean enabled;
    private Timestamp created;
    private Timestamp updated;

    public UserResponseDTO(Long id, String username, Double dollar_balance, boolean enabled, Timestamp created_on, Timestamp updated_on) {
        this.id = id;
        this.username = username;
        this.dollarBalance = dollar_balance;
        this.enabled = enabled;
        this.created = created_on;
        this.updated = updated_on;
    }

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.dollarBalance = user.getDollarBalance();
        this.enabled = user.isEnabled();
        this.created = user.getCreated();
        this.updated = user.getUpdated();
    }
}
