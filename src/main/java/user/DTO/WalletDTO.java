package user.DTO;

import user.model.User;
import user.model.UserOrders;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;

import java.sql.Timestamp;
import java.util.function.LongPredicate;

public class WalletDTO {

    private Long id_stock;
    private Long id_user;
    private String stock_symbol;
    private String stock_name;
    private Long volume;
    private boolean enabled;
    private Timestamp created_on;
    private Timestamp updated_on;

    public WalletDTO(){}

    public WalletDTO(Long id_user,Long id_stock, String stock_symbol, String stock_name, Long volume, boolean enabled, Timestamp created_on, Timestamp updated_on) {

        this.id_user = id_user;
        this.id_stock = id_stock;
        this.stock_symbol = stock_symbol;
        this.stock_name = stock_name;
        this.volume = volume;
        this.enabled = enabled;
        this.created_on = created_on;
        this.updated_on = updated_on;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
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

    public UserStockBalances transformaObjeto(User user){
        return new UserStockBalances( new UserStockBalancesId(user, id_stock),  stock_symbol,  stock_name,  volume, enabled);
    }

    public Long getId_stock() {
        return id_stock;
    }

    public void setId_stock(Long id_stock) {
        this.id_stock = id_stock;
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
}
