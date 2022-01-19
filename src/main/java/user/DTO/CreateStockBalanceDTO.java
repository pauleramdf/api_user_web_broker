package user.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import user.model.User;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;

@Getter
@Setter
public class CreateStockBalanceDTO {

    private Long id_stock;
    private Long id_user;
    private String stock_symbol;
    private String stock_name;
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
        return new UserStockBalances(new UserStockBalancesId(user, id_stock), stock_symbol, stock_name, volume,true);
    }

}
