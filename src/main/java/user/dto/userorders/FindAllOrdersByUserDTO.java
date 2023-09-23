package user.dto.userorders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import user.model.UserOrders;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindAllOrdersByUserDTO {

    private Long id;
    private Long idUser;
    private Long idStock;
    private String stockSymbol;
    private String stockName;
    private Long volume;
    private Double price;
    private Double totalPrice;
    private Long remainingVolume;
    private Integer type;
    private Integer status;
    private Timestamp created;
    private Timestamp updated;

    public FindAllOrdersByUserDTO(UserOrders u) {
        this.id = u.getId();
        this.idUser = u.getUser().getId();
        this.idStock = u.getIdStock();
        this.stockSymbol = u.getStockSymbol();
        this.stockName = u.getStockName();
        this.volume = u.getVolume();
        this.price = u.getPrice();
        this.totalPrice = u.getTotalPrice();
        this.remainingVolume = u.getRemainingVolume();
        this.type = u.getType().getValue();
        this.status = u.getStatus();
        this.created = u.getCreated();
        this.updated = u.getUpdated();
    }
}
