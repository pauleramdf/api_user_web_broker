package user.controller;

import net.bytebuddy.implementation.bytecode.Throw;
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
import user.DTO.WalletDTO;
import user.model.User;
import user.model.UserOrders;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;
import user.repository.UserOrdersRepository;
import user.repository.UserRepository;
import user.repository.UserStockBalancesRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

    @PostMapping("/order")
    public String createOrder(@Valid @RequestBody CreateOrdersDTO order, @RequestHeader("Authorization") String token, Principal principal) {


        User user = userRepository.findById(order.getId_user()).orElseThrow(Error::new);
        UserOrders createdOrder = order.transformaParaObjeto(user);
        try {

            UserStockBalances wallet
                    = walletRepository.find(user.getId(), order.getId_stock()).orElse(new UserStockBalances( user, order.getId_stock(), order.getStock_symbol(), order.getStock_name(), Long.valueOf(0), true));


            walletRepository.saveWallet(user.getId(), wallet.getId_stock(), wallet.getStock_symbol(), wallet.getStock_name(), wallet.getVolume(), wallet.isEnabled());
            if (order.getType() == 1) {
                //encontra todas as ordens abertas de compra para essa mesma stock
                List<UserOrders> orders = orderRepository.findAllBuyOrders(order.getStock_name());

                if (orders.isEmpty()) {

                    setMaxMin(order, token);
                    orderRepository.save(createdOrder);
                    return ("ordem de venda criada com sucesso");
                } else {
                    for (UserOrders o : orders) {
                        if (o.getPrice() >= createdOrder.getPrice() && o.getRemainingVolume() <= createdOrder.getRemainingVolume()) {
                            createdOrder.setRemainingVolume(createdOrder.getRemainingVolume() - o.getRemainingVolume());
                            o.setRemainingVolume(Long.valueOf(0));
                            orderRepository.save(o);
                            wallet.setVolume(o.getRemainingVolume() + wallet.getVolume());
                            //credita o valor na carteira

                        }
                        if (o.getPrice() >= createdOrder.getPrice() && o.getRemainingVolume() >= createdOrder.getRemainingVolume()) {
                            //atualiza o volume restante na ordem de venda
                            createdOrder.setRemainingVolume(Long.valueOf(0));
                            //atualiza o volume restante na ordem de compra
                            o.setRemainingVolume(o.getRemainingVolume() - createdOrder.getRemainingVolume());
                            orderRepository.save(o);
                            //credita o valor na carteira
                            wallet.setVolume(createdOrder.getRemainingVolume() + wallet.getVolume());
                        }
                        if (createdOrder.getRemainingVolume() == Long.valueOf(0)) {
                            //atualiza o status da ordem para inativa
                            createdOrder.setStatus(0);
                            orderRepository.save(createdOrder);
                            break;
                        }
                    }
                    //salva a ordem mantendo o status atual seja inativo ou ativa
                    orderRepository.save(createdOrder);
                    //salva a carteira
                    walletRepository.save(wallet);

                }
                //implementar logica para a venda
                //checa se existe alguma ordem de compra com o valor igual ou superior
                //se exister, debita o volume necessario
                //após isso credita o valor na conta do user
                //se vender o volume total, a ordem eh encerrada
                //se não existir a ordem a venda fica aberta

            } else {
                List<UserOrders> orders = orderRepository.findAllSellOrders(order.getStock_name());
                if (orders.isEmpty()) {
                    orderRepository.save(order.transformaParaObjeto(user));
                    return ("ordem de compra criada com sucesso");
                } else {
                    //todo
                }
            }
            return "falha ao criar ordem";
        }
        catch (Error e){
            throw new Error(e);
        }
    }

    public void setMaxMin(CreateOrdersDTO order, String token){

        List<UserOrders> orders = orderRepository.findAllSellOrders(order.getStock_name());
        UserOrders maxOrder = orders.stream().max(Comparator.comparing(UserOrders::getPrice)).orElseThrow();
        UserOrders minOrder = orders.stream().min(Comparator.comparing(UserOrders::getPrice)).orElseThrow();

        StockPricesDTO stockPrices = new StockPricesDTO(order.getId_stock(), order.getStock_symbol(), order.getStock_name(), minOrder.getPrice(), maxOrder.getPrice());

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
}

//        Mono<StockDTO[]> stock =  this.webClient
//                .get()
//                .uri("/stocks")
//                .header(HttpHeaders.AUTHORIZATION, token)
//                .retrieve()
//                .bodyToMono(StockDTO[].class);
//        List lista = Arrays.stream(stock.block()).toList();
