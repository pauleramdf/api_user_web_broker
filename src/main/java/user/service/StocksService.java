package user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import user.dto.stocks.StockDTO;
import user.dto.stocks.StockPricesDTO;

@Service("stocksService")
@RequiredArgsConstructor
public class StocksService {
    private final WebClient webClient;

    public void updateAskBid(StockPricesDTO stockPrices, String token) {
        Mono<StockDTO> stock = this.webClient
                .post()
                .uri("/stocks/askbid")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(stockPrices), StockPricesDTO.class)
                .retrieve()
                .bodyToMono(StockDTO.class);

        stock.block();
    }

    public StockDTO getStock(Long id, String token) {
        Mono<StockDTO> str = this.webClient
                .get()
                .uri("/stocks/" + id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(StockDTO.class);
        return str.block();
    }
}
