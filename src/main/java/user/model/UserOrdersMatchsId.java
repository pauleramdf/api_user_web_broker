package user.model;

import lombok.*;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserOrdersMatchsId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "id_sell_order")
    private UserOrders sellOrders;
    @ManyToOne
    @JoinColumn(name = "id_buy_order")
    private UserOrders buyOrders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserOrdersMatchsId that = (UserOrdersMatchsId) o;
        return Objects.equals(sellOrders, that.sellOrders) && Objects.equals(buyOrders, that.buyOrders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellOrders, buyOrders);
    }
}

