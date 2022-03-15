package user.dto.stocks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class StockDTO {
    private Long Id;
    private String stockSymbol;
    private String stockName;
    private BigDecimal askMin;
    private BigDecimal askMax;
    private BigDecimal bidMin;
    private BigDecimal bidMax;
    private Timestamp created;
    private Timestamp updated;

    public StockDTO(){
    }
}
