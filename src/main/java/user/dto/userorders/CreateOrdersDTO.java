package user.dto.userorders;

import lombok.Getter;
import lombok.Setter;
import user.model.User;
import user.model.UserOrders;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Getter
@Setter
public class CreateOrdersDTO {

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

    @Min(0)
    @Max(1)
    @Valid
    @NotNull
    private Integer type;

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
}
