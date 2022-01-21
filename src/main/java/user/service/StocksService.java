package user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import user.DTO.CreateOrdersDTO;
import user.DTO.MaxMinDTO;
import user.DTO.StockDTO;
import user.DTO.StockPricesDTO;

@Service("stocksService")
public class StocksService {
    @Autowired
    private WebClient webClient;
    @Autowired
    private OrderService orderService;


    public void setMaxMinBuy(CreateOrdersDTO order, String token){
        StockPricesDTO stockPrices;
        MaxMinDTO maxmin = orderService.findMaxMinBuyOrders(order.getId_stock());
        if(maxmin.getMaxPrice() == null){
            stockPrices = new StockPricesDTO(order.getId_stock(), order.getStock_symbol(), order.getStock_name(), order.getPrice(), order.getPrice());
        }
        else{
            stockPrices = new StockPricesDTO(order.getId_stock(), order.getStock_symbol(), order.getStock_name(), maxmin.getMinPrice(), maxmin.getMaxPrice());
        }
        Mono<String> str = this.webClient
                .post()
                .uri("/stocks/buy")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(stockPrices))
                .retrieve()
                .bodyToMono(String.class);
        System.out.println(str.block());
    }


    public void setMaxMinSell(CreateOrdersDTO order, String token){
        StockPricesDTO stockPrices;
        MaxMinDTO maxmin = orderService.findMaxMinSellOrders(order.getId_stock());
        if(maxmin.getMaxPrice() == null){
            stockPrices = new StockPricesDTO(order.getId_stock(), order.getStock_symbol(), order.getStock_name(), order.getPrice(), order.getPrice());
        }
        else{
            stockPrices = new StockPricesDTO(order.getId_stock(), order.getStock_symbol(), order.getStock_name(), maxmin.getMinPrice(), maxmin.getMaxPrice());
        }
        Mono<String> str = this.webClient
                .post()
                .uri("/stocks/sell")
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(stockPrices))
                .retrieve()
                .bodyToMono(String.class);
        System.out.println(str.block());
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
}
