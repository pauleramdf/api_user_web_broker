package user.dto.stocks;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class StockPricesDTO {

    private Long id_stock;
    private Double ask_min;
    private Double ask_max;
    private Double bid_min;
    private Double bid_max;

    public StockPricesDTO(Long id_stock, Double ask_min, Double ask_max, Double bid_min, Double bid_max) {
        this.id_stock = id_stock;
        this.ask_min = ask_min;
        this.ask_max = ask_max;
        this.bid_min = bid_min;
        this.bid_max = bid_max;
    }
}
