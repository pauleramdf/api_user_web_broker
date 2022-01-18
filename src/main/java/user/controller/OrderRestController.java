package user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import user.DTO.CreateOrdersDTO;
import user.DTO.StockPricesDTO;
import user.model.User;
import user.model.UserOrders;
import user.repository.UserOrdersRepository;
import user.repository.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@ComponentScan("com.user.repository")
public class OrderRestController {
    @Autowired
    private WebClient webClient;
    @Autowired
    private UserOrdersRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/order")
    public String createOrder(@Valid @RequestBody CreateOrdersDTO order, @RequestHeader("Authorization") String token, Principal principal) {
//        Mono<StockDTO[]> stock =  this.webClient
//                .get()
//                .uri("/stocks")
//                .header(HttpHeaders.AUTHORIZATION, token)
//                .retrieve()
//                .bodyToMono(StockDTO[].class);
//        List lista = Arrays.stream(stock.block()).toList();

        User user = userRepository.findById(order.getId_user()).orElseThrow(Error::new);
        if(order.getType() == 1){
            List<UserOrders> orders = orderRepository.findAllBuyOrders(order.getStock_name());
            if(orders.isEmpty()){
                orderRepository.save(order.transformaParaObjeto(user));
                StockPricesDTO stockPrices = new StockPricesDTO(order.getId_stock(), order.getStock_symbol(), order.getStock_name(), order.getPrice());
                System.out.println(stockPrices.getPrice());

                Mono<String> str = this.webClient
                        .post()
                        .uri("/stocks/sell")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(stockPrices))
                        .retrieve()
                        .bodyToMono(String.class);
                System.out.println(str.block());
                return("ordem de venda criada com sucesso");
            }
            else{
                //todo
            }
            //implementar logica para a venda
            //checa se existe alguma ordem de compra com o valor igual ou superior
            //se exister, debita o volume necessario
            //após isso credita o valor na conta do user
            //se vender o volume total, a ordem eh encerrada
            //se não existir a ordem a venda fica aberta
            System.out.println(orders);


        }
        else{
            List<UserOrders> orders = orderRepository.findAllSellOrders(order.getStock_name());
            if(orders.isEmpty()){
                orderRepository.save(order.transformaParaObjeto(user));
                return("ordem de compra criada com sucesso");
            }else{
                //todo
            }
        }
        return "falha ao criar ordem";
    }
}
