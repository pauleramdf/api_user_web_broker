package user.DTO;

import lombok.Getter;
import lombok.Setter;
import user.model.User;
import user.model.UserOrders;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;

import java.sql.Timestamp;

@Getter
@Setter
public class WalletDTO {

    private Long id_stock;
    private Long id_user;
    private String stock_symbol;
    private String stock_name;
    private Long volume;
    private Timestamp created_on;
    private Timestamp updated_on;

    public WalletDTO(){}

    public WalletDTO(Long id_user,Long id_stock, String stock_symbol, String stock_name, Long volume, Timestamp created_on, Timestamp updated_on) {

        this.id_user = id_user;
        this.id_stock = id_stock;
        this.stock_symbol = stock_symbol;
        this.stock_name = stock_name;
        this.volume = volume;
        this.created_on = created_on;
        this.updated_on = updated_on;
    }

    public UserStockBalances transformaObjeto(User user){
        return new UserStockBalances( new UserStockBalancesId(user, id_stock),  stock_symbol,  stock_name,  volume);
    }


}
