package user.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UserStockBalancesId implements Serializable {
    private static final long serialversionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    private Long id_stock;

    public UserStockBalancesId() {
    }

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