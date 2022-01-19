package user.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="user_stock_balances")
public class UserStockBalances implements Serializable {

    @EmbeddedId
    private UserStockBalancesId id;

    private String stock_symbol;
    private String stock_name;
    private Long volume;
    private boolean enabled;
    private Timestamp created_on;
    private Timestamp updated_on;

    public UserStockBalances(){
        this.created_on = Timestamp.valueOf(LocalDateTime.now());
        this.updated_on = Timestamp.valueOf(LocalDateTime.now());
        this.volume = Long.valueOf(0);
    }
    public UserStockBalances(UserStockBalancesId id, String stock_symbol, String stock_name, Long volume, boolean enabled) {
        this.id = id;
        this.stock_symbol = stock_symbol;
        this.stock_name = stock_name;
        this.volume = volume;
        this.created_on = Timestamp.valueOf(LocalDateTime.now());
        this.updated_on = Timestamp.valueOf(LocalDateTime.now());
        this.enabled = enabled;
    }

    public UserStockBalances(UserStockBalancesId id, String stock_symbol, String stock_name, Long volume, boolean enabled, Timestamp created_on, Timestamp updated_on) {
        this.id = id;
        this.stock_symbol = stock_symbol;
        this.stock_name = stock_name;
        this.volume = volume;
        this.enabled = enabled;
        this.created_on = created_on;
        this.updated_on = updated_on;
    }
}


