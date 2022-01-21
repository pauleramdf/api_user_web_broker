package user.DTO;

import lombok.Getter;
import lombok.Setter;
import user.model.UserStockBalancesId;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
public class FindAllByUserDTO {

    private UserStockBalancesId id;
    private String stock_symbol;
    private String stock_name;
    private Long volume;
    private Timestamp created_on;
    private Timestamp updated_on;

    public FindAllByUserDTO() {
    }

    public FindAllByUserDTO( UserStockBalancesId id, String stock_symbol, String stock_name, Long volume) {
        this.id = id;
        this.stock_symbol = stock_symbol;
        this.stock_name = stock_name;
        this.volume = volume;
        this.created_on = created_on;
        this.updated_on = updated_on;
    }

    public FindAllByUserDTO(UserStockBalancesId id, String stock_symbol, String stock_name, Long volume, Timestamp created_on, Timestamp updated_on) {
        this.id = id;
        this.stock_symbol = stock_symbol;
        this.stock_name = stock_name;
        this.volume = volume;
        this.created_on = created_on;
        this.updated_on = updated_on;
    }
}
