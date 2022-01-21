package user.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockPricesDTO {

    private Long Id;
    private String stock_symbol;
    private String stock_name;
    private double minPrice;
    private double maxPrice;


    public StockPricesDTO(){};

    public StockPricesDTO(Long id, String stock_symbol, String stock_name, double minPrice, double maxPrice) {
        Id = id;
        this.stock_symbol = stock_symbol;
        this.stock_name = stock_name;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

}
