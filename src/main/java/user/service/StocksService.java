package user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import user.dto.stocks.StockPricesDTO;

@Service("stocksService")
@RequiredArgsConstructor
public class StocksService {
    private final KafkaService kafkaService;

    public void updateAskBid(StockPricesDTO stockPrices, String token) {
        kafkaService.sendStockPricesDTO(stockPrices.getIdStock().toString(), stockPrices);
    }
}
