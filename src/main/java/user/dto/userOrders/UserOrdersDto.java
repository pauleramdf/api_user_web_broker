package user.dto.userOrders;

import lombok.Data;
import user.model.User;
import user.model.UserOrders;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class UserOrdersDto {
    @NotNull
    private Integer type;
    @NotNull
    private Integer status;
    @NotBlank
    private String username;
    @Min(1)
    @NotNull
    private Long id_stock;
    @NotBlank
    private String stock_name;
    @NotBlank
    private String stock_symbol;
    @Min(1)
    @NotNull
    private Long volume;
    @Min(1)
    @NotNull
    private Double price;
    @Min(1)
    @NotNull
    private Long remainingVolume;
    @Min(1)
    @NotNull
    private Double totalPrice;

    private Timestamp created_on;
    private Timestamp updated_on;

    public UserOrdersDto (UserOrders orders){
        this.username = orders.getUser().getUsername();
        this.id_stock = orders.getId_stock();
        this.stock_symbol = orders.getStock_symbol();
        this.stock_name = orders.getStock_name();
        this.volume = orders.getVolume();
        this.price = orders.getPrice();
        this.totalPrice = orders.getTotalPrice();
        this.remainingVolume = orders.getRemainingVolume();
        this.type = orders.getType();
        this.status = orders.getStatus();
        this.created_on = orders.getCreated_on();
        this.updated_on = orders.getUpdated_on();
    }
}
