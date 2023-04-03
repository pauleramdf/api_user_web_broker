package user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
   private String token;
   private Long id;
   private String fullName;
   private String username;
   private Double dollarBalance;
   private Timestamp created;
   private Timestamp updated;
}
