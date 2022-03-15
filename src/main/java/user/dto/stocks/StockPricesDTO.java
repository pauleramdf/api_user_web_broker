package user.dto.stocks;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockPricesDTO {

    private Long idStock;
    private Double askMin;
    private Double askMax;
    private Double bidMin;
    private Double bidMax;

    public StockPricesDTO(Long idStock, Double askMin, Double askMax, Double bidMin, Double bidMax) {
        this.idStock = idStock;
        this.askMin = askMin;
        this.askMax = askMax;
        this.bidMin = bidMin;
        this.bidMax = bidMax;
    }
}
