package user.dto.user;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TokenDTO {
    String token;
    Timestamp expiresIn;
}
