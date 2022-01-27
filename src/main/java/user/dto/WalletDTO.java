package user.dto;

import lombok.Getter;
import lombok.Setter;
import user.model.User;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Getter
@Setter
public class WalletDTO {

    private Long id_stock;
    @NotBlank
    private String username;
    private String stock_symbol;
    private String stock_name;
    private Long volume;
    private Timestamp created_on;
    private Timestamp updated_on;

    public WalletDTO(){}

    public WalletDTO(String username,Long id_stock, String stock_symbol, String stock_name, Long volume, Timestamp created_on, Timestamp updated_on) {

        this.username = username;
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
