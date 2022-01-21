package user.DTO;

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
    private Long id_user;
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
    private Long remaining_volume;
    @Min(1)
    @NotNull
    private Double total_price;

    @Min(0)
    @Max(1)
    @Valid
    @NotNull
    private Integer type;

    public UserOrders transformaParaObjeto(User user){
        return new UserOrders(user, id_stock, stock_symbol, stock_name, volume, price, total_price, remaining_volume, type, 1);
    }
}
