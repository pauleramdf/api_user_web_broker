package user.dto.userstockbalances;

import lombok.Getter;
import lombok.Setter;
import user.model.User;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;

import java.sql.Timestamp;

@Getter
@Setter
public class WalletDTO {

    private Long idStock;
    private String stockSymbol;
    private String stockName;
    private Long volume;
    private Timestamp created;
    private Timestamp updated;

    public WalletDTO(){}

    public WalletDTO(Long idStock, String stockSymbol, String stockName, Long volume, Timestamp created, Timestamp updated) {

        this.idStock = idStock;
        this.stockSymbol = stockSymbol;
        this.stockName = stockName;
        this.volume = volume;
        this.created = created;
        this.updated = updated;
    }

    public WalletDTO(UserStockBalances wallet) {
        this.idStock = wallet.getId().getIdStock();
        this.stockSymbol = wallet.getStockSymbol();
        this.stockName = wallet.getStockName();
        this.volume = wallet.getVolume();
        this.created = wallet.getCreated();
        this.updated = wallet.getUpdated();
    }

    public UserStockBalances transformaObjeto(User user){
        return new UserStockBalances( new UserStockBalancesId(user, idStock),  stockSymbol,  stockName,  volume);
    }


}
