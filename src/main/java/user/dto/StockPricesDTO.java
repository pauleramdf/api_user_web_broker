package user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockPricesDTO {

    private Long id_stock;
    private Double askMin;
    private Double askMax;
    private Double bidMin;
    private Double bidMax;


    public StockPricesDTO(){};

    public StockPricesDTO(Long id_stock, Double askMin, Double askMax, Double bidMin, Double bidMax) {
        this.id_stock = id_stock;
        this.askMin = askMin;
        this.askMax = askMax;
        this.bidMin = bidMin;
        this.bidMax = bidMax;
    }
}
