package user.dto.userOrders;

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

    private Long Id;
    private Long id_user;
    private Long id_stock;
    private String stock_symbol;
    private String stock_name;
    private Long volume;
    private Double price;
    private Double totalPrice;
    private Long remainingVolume;
    private Integer type;
    private Integer status;
    private Timestamp created_on;
    private Timestamp updated_on;

    public FindAllOrdersByUserDTO(UserOrders u) {
        this.Id = u.getId();
        this.id_user = u.getUser().getId();
        this.id_stock = u.getId_stock();
        this.stock_symbol = u.getStock_symbol();
        this.stock_name = u.getStock_name();
        this.volume = u.getVolume();
        this.price = u.getPrice();
        this.totalPrice = u.getTotalPrice();
        this.remainingVolume = u.getRemainingVolume();
        this.type = u.getType();
        this.status = u.getStatus();
        this.created_on = u.getCreated_on();
        this.updated_on = u.getUpdated_on();
    }
}
