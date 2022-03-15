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
    private Long id;
    @NotNull
    private Integer type;
    @NotNull
    private Integer status;
    @NotBlank
    private String username;
    @Min(1)
    @NotNull
    private Long idStock;
    @NotBlank
    private String stockName;
    @NotBlank
    private String stockSymbol;
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

    private Timestamp created;
    private Timestamp updated;

    public UserOrdersDto (UserOrders orders){
        this.id = orders.getId();
        this.username = orders.getUser().getUsername();
        this.idStock = orders.getIdStock();
        this.stockSymbol = orders.getStockSymbol();
        this.stockName = orders.getStockName();
        this.volume = orders.getVolume();
        this.price = orders.getPrice();
        this.totalPrice = orders.getTotalPrice();
        this.remainingVolume = orders.getRemainingVolume();
        this.type = orders.getType();
        this.status = orders.getStatus();
        this.created = orders.getCreated();
        this.updated = orders.getUpdated();
    }
}
