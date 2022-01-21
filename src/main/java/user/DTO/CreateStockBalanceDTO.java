package user.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import user.model.User;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateStockBalanceDTO {


    @Min(1)
    @NotNull
    private Long id_stock;
    @Min(1)
    @NotNull
    private Long id_user;
    @NotBlank
    private String stock_symbol;
    @NotBlank
    private String stock_name;
    @Min(1)
    @NotNull
    private Long volume;

    public CreateStockBalanceDTO(){

    }
    public CreateStockBalanceDTO(Long id_user, Long id_stock, String stock_symbol, String stock_name, Long volume) {

        this.id_user = id_user;
        this.id_stock = id_stock;
        this.stock_symbol = stock_symbol;
        this.stock_name = stock_name;
        this.volume = volume;
    }

    public UserStockBalances transformaDTO(User user) {
        return new UserStockBalances(new UserStockBalancesId(user, id_stock), stock_symbol, stock_name, volume);
    }

}
