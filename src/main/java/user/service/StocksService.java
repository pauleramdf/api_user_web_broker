package user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import user.dto.MaxMinDTO;
import user.dto.StockDTO;
import user.dto.StockPricesDTO;

@Service("stocksService")
public class StocksService {
    @Autowired
    private WebClient webClient;
    @Autowired
    private OrderService orderService;


    public void setAskBid(StockPricesDTO stockPrices, String token){
        Mono<StockDTO> stock = this.webClient
                .post()
                .uri("/stocks/askbid")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(stockPrices))
                .retrieve()
                .bodyToMono(StockDTO.class);

        stock.block();
    }

    public StockDTO getStock(Long id, String token){
        Mono<StockDTO> str = this.webClient
                .get()
                .uri("/stocks/"+id)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(StockDTO.class);
        return str.block();
    }

    public void updateAskBid(Long id_stock,String token) {

        System.out.println("id da stock " +id_stock);
        MaxMinDTO bid = orderService.findMaxMinBuyOrders(id_stock);
        MaxMinDTO ask = orderService.findMaxMinSellOrders(id_stock);

        System.out.println(bid.getMaxPrice() +"   "+ bid.getMinPrice());
        System.out.println(ask.getMaxPrice() +"   "+ ask.getMinPrice());

        StockPricesDTO stockPrices = new StockPricesDTO();
        stockPrices.setId_stock(id_stock);
        stockPrices.setAskMax(ask.getMaxPrice());
        stockPrices.setAskMin(ask.getMinPrice());
        stockPrices.setBidMax(bid.getMaxPrice());
        stockPrices.setBidMin(bid.getMinPrice());

//        System.out.println("ue "+ stockPrices.getMaxPrice() +"   "+ stockPrices.getMinPrice());
//        System.out.println("ue "+stockPrices.getMaxPrice() +"   "+ stockPrices.getMinPrice());
        setAskBid(stockPrices, token);
    }
}
