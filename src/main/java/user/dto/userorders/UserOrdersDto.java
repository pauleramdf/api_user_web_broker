package user.dto.userorders;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import user.model.User;
import user.model.UserOrders;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrdersDto {
    private Long id;
    @NotNull
    private Integer type;
    private Integer status;
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

    public UserOrders transformaParaObjeto(User user){
        UserOrders order = new UserOrders();
        order.setUser(user);
        order.setIdStock(idStock);
        order.setStockName(stockName);
        order.setStockSymbol(stockSymbol);
        order.setVolume(volume);
        order.setPrice(price);
        order.setTotalPrice(totalPrice);
        order.setRemainingVolume(remainingVolume);
        order.setType(type);
        order.setStatus(1);
        return order;
    }

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
