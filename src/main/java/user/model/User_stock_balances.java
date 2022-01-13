package user.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="user_stock_balances")
@IdClass(User_stock_balances_id.class)
public class User_stock_balances implements Serializable {

    @Id
    private Long id_stock;

    @ManyToOne
    @JoinColumn(name="id_user")
    @Id
    private User user;
    private String stock_symbol;
    private String stock_name;
    private Long volume;
    private boolean enabled;

    @CreatedDate
    @Column(name = "created_on")
    private Timestamp created_on;
    @LastModifiedDate
    @Column(name = "updated_on")
    private Timestamp updated_on;

    public User_stock_balances(){
    }
    public User_stock_balances(User user, Long id_stock, String stock_symbol, String stock_name, Long volume, boolean enabled) {
        this.id_stock = id_stock;
        this.user = user;
        this.stock_symbol = stock_symbol;
        this.stock_name = stock_name;
        this.volume = volume;
        this.enabled = enabled;
    }

}


