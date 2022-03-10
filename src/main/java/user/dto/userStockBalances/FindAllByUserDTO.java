package user.dto.userStockBalances;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import user.model.UserStockBalances;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class FindAllByUserDTO {

    private Long id_user;
    private Long id_stock;
    private String stock_symbol;
    private String stock_name;
    private Long volume;
    private Timestamp created_on;
    private Timestamp updated_on;

    public FindAllByUserDTO(UserStockBalances wallet) {
        this.id_user = wallet.getId().getUser().getId();
        this.id_stock = wallet.getId().getId_stock();
        this.stock_symbol = wallet.getStock_symbol();
        this.stock_name = wallet.getStock_name();
        this.volume = wallet.getVolume();
        this.created_on = wallet.getCreated_on();
        this.updated_on = wallet.getUpdated_on();
    }

}
