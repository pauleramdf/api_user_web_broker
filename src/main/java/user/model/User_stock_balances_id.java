package user.model;

import java.io.Serializable;
import java.util.Objects;

public class User_stock_balances_id implements Serializable {
    private User user;

    private Long id_stock;

    public User_stock_balances_id(User user, Long id_stock) {
        this.user = user;
        this.id_stock = id_stock;
    }

    @Override
    public boolean equals(Object x) {
        if(this == x){
            return true;
        }

        if(!(x instanceof User_stock_balances_id)){
            return false;
        }

        User_stock_balances_id y = (User_stock_balances_id) x;
        return this.user == y.user && this.id_stock == y.id_stock;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, id_stock);
    }
}