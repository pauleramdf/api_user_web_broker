package user.dto.stocks;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class StockDTO {
    private Long Id;
    private String stock_symbol;
    private String stock_name;
    private BigDecimal ask_min;
    private BigDecimal ask_max;
    private BigDecimal bid_min;
    private BigDecimal bid_max;
    private Timestamp created_on;
    private Timestamp updated_on;

    public StockDTO(){
    }
    public StockDTO(Long id, String stock_symbol, String stock_name, BigDecimal ask_min, BigDecimal ask_max, BigDecimal bid_min, BigDecimal bid_max, Timestamp created_on, Timestamp updated_on) {
        Id = id;
        this.stock_symbol = stock_symbol;
        this.stock_name = stock_name;
        this.ask_min = ask_min;
        this.ask_max = ask_max;
        this.bid_min = bid_min;
        this.bid_max = bid_max;
        this.created_on = created_on;
        this.updated_on = updated_on;
    }

}
