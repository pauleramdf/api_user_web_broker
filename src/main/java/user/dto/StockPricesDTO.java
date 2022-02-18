package user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class StockPricesDTO {

    private Long id_stock;
    private Double ask_min;
    private Double ask_max;
    private Double bid_min;
    private Double bid_max;

    
}
