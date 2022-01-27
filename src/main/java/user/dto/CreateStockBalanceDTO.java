package user.dto;

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
    private Long id_stock;
    @NotBlank
    private String username;
    @NotBlank
    private String stock_symbol;
    @NotBlank
    private String stock_name;
    @Min(1)
    @NotNull
    private Long volume;


    public UserStockBalances transformaDTO(User user) {
        UserStockBalances u = new UserStockBalances(new UserStockBalancesId(user, id_stock), stock_symbol, stock_name, Long.valueOf(0), Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()));
        return u;
    }

}
