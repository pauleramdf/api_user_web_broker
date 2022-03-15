package user.dto.userstockbalances;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import user.model.User;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateStockBalanceDTO {


    @Min(1)
    @NotNull
    private Long idStock;
    @NotBlank
    private String username;
    @NotBlank
    private String stockSymbol;
    @NotBlank
    private String stockName;
    @Min(1)
    @NotNull
    private Long volume;


    public UserStockBalances transformaDTO(User user) {
        return new UserStockBalances(new UserStockBalancesId(user, idStock), stockSymbol, stockName, volume, Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()));
    }

}
