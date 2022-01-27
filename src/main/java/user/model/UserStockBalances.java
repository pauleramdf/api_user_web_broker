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
    private static final long serialversionUID = 1L;

    @EmbeddedId
    private UserStockBalancesId id;

    @Column(name = "stock_symbol")
    private String stock_symbol;

    @Column(name = "stock_name")
    private String stock_name;

    @Column(name = "volume")
    private Long volume;

    @CreationTimestamp
    @Column(name = "created_on")
    private Timestamp created_on;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updated_on;

    public UserStockBalances(){
        this.volume = Long.valueOf(0);
    }

    public UserStockBalances(UserStockBalancesId id, String stock_symbol, String stock_name, Long volume) {
        this.id = id;
        this.stock_symbol = stock_symbol;
        this.stock_name = stock_name;
        this.volume = volume;
    }

    public UserStockBalances(UserStockBalancesId id, String stock_symbol, String stock_name, Long volume,Timestamp created_on, Timestamp updated_on) {
        this.id = id;
        this.stock_symbol = stock_symbol;
        this.stock_name = stock_name;
        this.volume = volume;
        this.created_on = created_on;
        this.updated_on = updated_on;

    }

}


