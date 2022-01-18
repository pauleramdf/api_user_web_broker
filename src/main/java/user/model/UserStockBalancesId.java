package user.model;

import java.io.Serializable;
import java.util.Objects;

public class UserStockBalancesId implements Serializable {
    private User user;

    private Long id_stock;

    public UserStockBalancesId(User user, Long id_stock) {
        this.user = user;
        this.id_stock = id_stock;
    }

    @Override
    public boolean equals(Object x) {
        if(this == x){
            return true;
        }

        if(!(x instanceof UserStockBalancesId)){
            return false;
        }

        UserStockBalancesId y = (UserStockBalancesId) x;
        return this.user == y.user && this.id_stock == y.id_stock;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, id_stock);
    }
}