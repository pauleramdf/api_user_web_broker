package user.dto;

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
    private Double dollar_balance;
    private boolean enabled;
    private Timestamp created_on;
    private Timestamp updated_on;

    public UserResponseDTO(Long id, String username, Double dollar_balance, boolean enabled, Timestamp created_on, Timestamp updated_on) {
        this.id = id;
        this.username = username;
        this.dollar_balance = dollar_balance;
        this.enabled = enabled;
        this.created_on = created_on;
        this.updated_on = updated_on;
    }

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.dollar_balance = user.getDollar_balance();
        this.enabled = user.isEnabled();
        this.created_on = user.getCreated_on();
        this.updated_on = user.getUpdated_on();
    }
}
