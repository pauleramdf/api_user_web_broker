package user.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class UserStockBalancesId implements Serializable {
    private static final long SERIALVERSIONUID = 1L;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    @Column(name = "id_stock")
    private Long idStock;

    public UserStockBalancesId() {
    }

    public UserStockBalancesId(User user, Long idStock) {
        this.user = user;
        this.idStock = idStock;
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
        return this.user == y.user && this.idStock == y.idStock;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, idStock);
    }
}