package user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import user.DTO.CreateOrdersDTO;
import user.DTO.MaxMinDTO;
import user.DTO.StockPricesDTO;
import user.model.User;
import user.model.UserOrders;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;
import user.repository.UserOrdersRepository;
import user.repository.UserRepository;
import user.repository.UserStockBalancesRepository;

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

    @Autowired
    private UserStockBalancesRepository walletRepository;
//
//    @PostMapping("/order")
//    public ResponseEntity<UserOrders> createOrder(@Valid @RequestBody CreateOrdersDTO order, @RequestHeader("Authorization") String token, Principal principal) {
//
//        //checa se o user existe de fato
//        User user = userRepository.findById(order.getId_user()).orElseThrow(Error::new);
//        UserOrders createdOrder = order.transformaParaObjeto(user);
//        UserStockBalances wallet
//                = walletRepository.find(user.getId(), order.getId_stock()).orElse(new UserStockBalances(new UserStockBalancesId(user, order.getId_stock()), order.getStock_symbol(), order.getStock_name(), Long.valueOf(0), true));
//
//        if (order.getType() == 1) {
//            return sellDomain(order, user, createdOrder, wallet, token);
//        }
//
//        else {
//            return buyDomain(order, user, createdOrder, wallet, token);
//        }
//    }
//
//    public ResponseEntity<UserOrders> sellDomain(CreateOrdersDTO order, User user, UserOrders createdOrder, UserStockBalances wallet, String token){
//
//        double balance = 0;
//        //encontra todas as ordens abertas de compra para essa mesma stock
//        List<UserOrders> orders = orderRepository.findAllBuyOrders(order.getStock_name());
//
//        //caso nao existam ordens de compra abertas para essa stock a ordem é salva.
//        if (orders.isEmpty()) {
//
//            setMaxMinSell(order, token);
//            orderRepository.save(createdOrder);
//            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
//        } else {
//            for (UserOrders o : orders) {
//                //checa se essa ordem tem preço compativel com a ordem de venda
//                if (o.getPrice() >= createdOrder.getPrice() && o.getRemainingVolume() <= createdOrder.getRemainingVolume()) {
//                    createdOrder.setRemainingVolume(createdOrder.getRemainingVolume() - o.getRemainingVolume());
//                    o.setRemainingVolume(Long.valueOf(0));
//                    orderRepository.save(o);
//                    wallet.setVolume( wallet.getVolume() - o.getRemainingVolume());
//                    balance = balance + o.getPrice();
//                    //credita o valor na carteira
//
//                }
//                if (o.getPrice() >= createdOrder.getPrice() && o.getRemainingVolume() >= createdOrder.getRemainingVolume()) {
//                    //atualiza o volume restante na ordem de venda
//                    createdOrder.setRemainingVolume(Long.valueOf(0));
//                    //atualiza o volume restante na ordem de compra
//                    o.setRemainingVolume(o.getRemainingVolume() - createdOrder.getRemainingVolume());
//                    orderRepository.save(o);
//                    //credita o valor na carteira
//                    wallet.setVolume(wallet.getVolume() - createdOrder.getRemainingVolume());
//                    balance = balance + o.getPrice();
//                }
//                //caso não reste volume a venda a ordem é desativada
//                if (createdOrder.getRemainingVolume() == Long.valueOf(0)) {
//                    //atualiza o status da ordem para inativa
//                    createdOrder.setStatus(0);
//                    orderRepository.save(createdOrder);
//                    break;
//                }
//            }
//            user.setDollar_balance(user.getDollar_balance()+balance);
//            userRepository.save(user);
//            //salva a ordem mantendo o status atual seja inativo ou ativa
//            orderRepository.save(createdOrder);
//            //salva a carteira
//            //walletRepository.saveWallet(user.getId(), wallet.getId_stock(), wallet.getStock_symbol(), wallet.getStock_name(), wallet.getVolume(), wallet.isEnabled());
//
//        }
//        //implementar logica para a venda
//            //checa se existe alguma ordem de compra com o valor igual ou superior
//            //se exister, debita o volume necessario
//            //após isso credita o valor na conta do user
//            //se vender o volume total, a ordem eh encerrada
//            //se não existir a ordem a venda fica aberta
//
//
//        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
//}
//
//    public ResponseEntity<UserOrders> buyDomain(CreateOrdersDTO order,User user, UserOrders createdOrder, UserStockBalances wallet, String token){
//        double balance = 0;
//        //encontra todas as ordens abertas de compra para essa mesma stock
//        List<UserOrders> orders = orderRepository.findAllSellOrders(order.getStock_name());
//
//        //caso nao existam ordens de compra abertas para essa stock a ordem é salva.
//        if (orders.isEmpty()) {
//
//            setMaxMinBuy(order, token);
//            orderRepository.save(createdOrder);
//            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
//        } else {
//            for (UserOrders o : orders) {
//                //checa se essa ordem tem preço compativel com a ordem de venda
//                if (o.getPrice() >= createdOrder.getPrice() && o.getRemainingVolume() <= createdOrder.getRemainingVolume()) {
//                    createdOrder.setRemainingVolume(createdOrder.getRemainingVolume() - o.getRemainingVolume());
//                    o.setRemainingVolume(Long.valueOf(0));
//                    orderRepository.save(o);
//                    wallet.setVolume( wallet.getVolume() - o.getRemainingVolume());
//                    balance = balance + o.getPrice();
//                    //credita o valor na carteira
//
//                }
//                if (o.getPrice() >= createdOrder.getPrice() && o.getRemainingVolume() >= createdOrder.getRemainingVolume()) {
//                    //atualiza o volume restante na ordem de venda
//                    createdOrder.setRemainingVolume(Long.valueOf(0));
//                    //atualiza o volume restante na ordem de compra
//                    o.setRemainingVolume(o.getRemainingVolume() - createdOrder.getRemainingVolume());
//                    orderRepository.save(o);
//                    //credita o valor na carteira
//                    wallet.setVolume(wallet.getVolume() - createdOrder.getRemainingVolume());
//                    balance = balance + o.getPrice();
//                }
//                //caso não reste volume a venda a ordem é desativada
//                if (createdOrder.getRemainingVolume() == Long.valueOf(0)) {
//                    //atualiza o status da ordem para inativa
//                    createdOrder.setStatus(0);
//                    orderRepository.save(createdOrder);
//                    break;
//                }
//            }
//            user.setDollar_balance(user.getDollar_balance()+balance);
//            userRepository.save(user);
//            //salva a ordem mantendo o status atual seja inativo ou ativa
//            orderRepository.save(createdOrder);
//            //salva a carteira
//            //walletRepository.saveWallet(user.getId(), wallet.getId_stock(), wallet.getStock_symbol(), wallet.getStock_name(), wallet.getVolume(), wallet.isEnabled());
//
//        }
//        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
//    }
//    @GetMapping("/order/buy/prices/{stock}")
//    public MaxMinDTO getPricesBuy(@PathVariable(value = "stock") String stock){
//        return orderRepository.findMaxMinBuyOrders(stock).orElse(new MaxMinDTO(Double.valueOf(0),Double.valueOf(0)));
//    }
//
//    @GetMapping("/order/sell/prices/{stock}")
//    public MaxMinDTO getPricesSell(@PathVariable(value = "stock") String stock){
//        return orderRepository.findMaxMinSellOrders(stock).orElse(new MaxMinDTO(Double.valueOf(0),Double.valueOf(0)));
//    }
//
//    public void setMaxMinSell(CreateOrdersDTO order, String token){
//        StockPricesDTO stockPrices;
//        MaxMinDTO maxmin = orderRepository.findMaxMinSellOrders(order.getStock_name()).orElse(new MaxMinDTO(Double.valueOf(0),Double.valueOf(0)));
//        if(maxmin.getMaxPrice() == null){
//            stockPrices = new StockPricesDTO(order.getId_stock(), order.getStock_symbol(), order.getStock_name(), order.getPrice(), order.getPrice());
//        }
//        else{
//            stockPrices = new StockPricesDTO(order.getId_stock(), order.getStock_symbol(), order.getStock_name(), maxmin.getMinPrice(), maxmin.getMaxPrice());
//        }
//        Mono<String> str = this.webClient
//                .post()
//                .uri("/stocks/sell")
//                .header(HttpHeaders.AUTHORIZATION, token)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromObject(stockPrices))
//                .retrieve()
//                .bodyToMono(String.class);
//        System.out.println(str.block());
//    }
//
//    public void setMaxMinBuy(CreateOrdersDTO order, String token){
//        StockPricesDTO stockPrices;
//        MaxMinDTO maxmin = orderRepository.findMaxMinBuyOrders(order.getStock_name()).orElse(new MaxMinDTO(Double.valueOf(0),Double.valueOf(0)));
//        if(maxmin.getMaxPrice() == null){
//            stockPrices = new StockPricesDTO(order.getId_stock(), order.getStock_symbol(), order.getStock_name(), order.getPrice(), order.getPrice());
//        }
//        else{
//            stockPrices = new StockPricesDTO(order.getId_stock(), order.getStock_symbol(), order.getStock_name(), maxmin.getMinPrice(), maxmin.getMaxPrice());
//        }
//        Mono<String> str = this.webClient
//                .post()
//                .uri("/stocks/buy")
//                .header(HttpHeaders.AUTHORIZATION, token)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(BodyInserters.fromObject(stockPrices))
//                .retrieve()
//                .bodyToMono(String.class);
//        System.out.println(str.block());
//    }
//}
//

}
//        Mono<StockDTO[]> stock =  this.webClient
//                .get()
//                .uri("/stocks")
//                .header(HttpHeaders.AUTHORIZATION, token)
//                .retrieve()
//                .bodyToMono(StockDTO[].class);
//        List lista = Arrays.stream(stock.block()).toList();
