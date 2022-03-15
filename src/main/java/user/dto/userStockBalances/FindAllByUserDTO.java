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

    private Long idStock;
    private String stockSymbol;
    private String stockName;
    private Long volume;
    private Timestamp created;
    private Timestamp updated;

    public FindAllByUserDTO(UserStockBalances wallet) {
        this.idStock = wallet.getId().getIdStock();
        this.stockSymbol = wallet.getStockSymbol();
        this.stockName = wallet.getStockName();
        this.volume = wallet.getVolume();
        this.created = wallet.getCreated();
        this.updated = wallet.getUpdated();
    }

}
