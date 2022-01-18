package user.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import user.model.User;

@Entity
@Table(name="user_stock_balances")
@IdClass(UserStockBalancesId.class)
public class UserStockBalances implements Serializable {

    @Id
    private Long id_stock;

    @ManyToOne(targetEntity = User.class)
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

    public UserStockBalances(){
        this.created_on = Timestamp.valueOf(LocalDateTime.now());
        this.updated_on = Timestamp.valueOf(LocalDateTime.now());
        this.volume = Long.valueOf(0);
    }
    public UserStockBalances(User user, Long id_stock, String stock_symbol, String stock_name, Long volume, boolean enabled) {

        this.user = user;
        this.id_stock = id_stock;
        this.stock_symbol = stock_symbol;
        this.stock_name = stock_name;
        this.volume = volume;
        this.created_on = Timestamp.valueOf(LocalDateTime.now());
        this.updated_on = Timestamp.valueOf(LocalDateTime.now());
        this.enabled = enabled;
    }

    public Long getId_stock() {
        return id_stock;
    }

    public void setId_stock(Long id_stock) {
        this.id_stock = id_stock;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStock_symbol() {
        return stock_symbol;
    }

    public void setStock_symbol(String stock_symbol) {
        this.stock_symbol = stock_symbol;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Timestamp getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Timestamp created_on) {
        this.created_on = created_on;
    }

    public Timestamp getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(Timestamp updated_on) {
        this.updated_on = updated_on;
    }
}


