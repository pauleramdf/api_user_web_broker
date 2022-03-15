package user.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name="user_stock_balances")
public class UserStockBalances implements Serializable {
    private static final long SERIALVERSIONUID = 1L;

    @EmbeddedId
    private UserStockBalancesId id;

    @Column(name = "stock_symbol")
    private String stockSymbol;

    @Column(name = "stock_name")
    private String stockName;

    @Column(name = "volume")
    private Long volume;

    @CreationTimestamp
    @Column(name = "created_on")
    private Timestamp created;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updated;

    public UserStockBalances(){
        this.volume = Long.valueOf(0);
    }

    public UserStockBalances(UserStockBalancesId id, String stockSymbol, String stockName, Long volume) {
        this.id = id;
        this.stockSymbol = stockSymbol;
        this.stockName = stockName;
        this.volume = volume;
    }

    public UserStockBalances(UserStockBalancesId id, String stockSymbol, String stockName, Long volume,Timestamp created, Timestamp updated) {
        this.id = id;
        this.stockSymbol = stockSymbol;
        this.stockName = stockName;
        this.volume = volume;
        this.created = created;
        this.updated = updated;

    }

}


