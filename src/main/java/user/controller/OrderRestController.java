package user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.DTO.CancelOrdersDTO;
import user.DTO.CreateOrdersDTO;
import user.DTO.MaxMinDTO;
import user.model.User;
import user.model.UserOrders;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;
import user.service.OrderService;
import user.service.StockBalanceService;
import user.service.StocksService;
import user.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@ComponentScan("com.user.repository")
@RequestMapping(consumes = "application/json")
public class OrderRestController {
    @Autowired
    private StockBalanceService stockBalanceService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StocksService stocksService;

    @Autowired
    private UserService userService;

    @PostMapping("/order")
    public ResponseEntity<UserOrders> createOrder( @RequestBody @Valid CreateOrdersDTO order, @RequestHeader("Authorization") String token, Principal principal) {

        //checa se o user existe de fato
        User user = userService.findById(order.getId_user()).orElseThrow(Error::new);
        //valida a transação
        validateTransaction(user, order);

        //cria ordem
        UserOrders createdOrder = order.transformaParaObjeto(user);

        //pega a carteira existente ou caso não exista cria uma zerada
        UserStockBalances wallet
                = stockBalanceService.findById(new UserStockBalancesId(user, order.getId_stock())).orElse(new UserStockBalances(new UserStockBalancesId(user, order.getId_stock()), order.getStock_symbol(), order.getStock_name(), Long.valueOf(0)));

        if (order.getType() == 1) {
            return sellDomain(order, user, createdOrder, wallet, token);
        }

        else {
            return buyDomain(order, user, createdOrder, wallet, token);
        }
    }

    private void validateTransaction(User user, CreateOrdersDTO order) {
//        UserStockBalances wallet =  stockBalanceService.findWallet(new UserStockBalancesId(user, order.getId_stock())).orElseThrow();
//
//        if(order.getType() == 1 &&  wallet.getVolume() < order.getVolume()){
//           //invalid
//            return;
//        }
//        if(order.getType() == 0 &&  user.getDollar_balance() < order.getPrice()){
//            //invalid
//            return;
//        }
        //valid
        return;
    }

    public ResponseEntity<UserOrders> sellDomain(CreateOrdersDTO order, User user, UserOrders createdOrder, UserStockBalances wallet, String token){

        //encontra todas as ordens abertas de compra para essa mesma stock
        List<UserOrders> orders = orderService.findAllBuyOrders(order.getId_stock());

        //caso nao existam ordens de compra abertas para essa stock a ordem é salva.
        if (orders.isEmpty()) {
            stocksService.setMaxMinSell(order, token);
            orderService.save(createdOrder);
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } else {
            for (UserOrders o : orders) {

                //checa se essa ordem tem preço compativel com a ordem de venda
                if (o.getPrice() >= createdOrder.getPrice() && o.getRemainingVolume() <= createdOrder.getRemainingVolume()) {

                    //subtrai da stockBalance o user atual
                    stockBalanceService.subVolumeWallet(wallet, o.getRemainingVolume());

                    orderService.subVolume(createdOrder, o.getRemainingVolume());

                    //credita o valor na carteira
                    userService.addDollarBalance(user, o.getPrice()* o.getRemainingVolume());

                    //acha a carteida do user dono da ordem de compra
                    UserStockBalances newWallet =  stockBalanceService.findWallet(new UserStockBalancesId(o.getUser(),o.getId_stock())).orElse(new UserStockBalances(new UserStockBalancesId(o.getUser(),o.getId_stock()), o.getStock_symbol(), o.getStock_name(), o.getVolume()));
                    //acrescenta na stockBalance do user dono da ordem de compra
                    stockBalanceService.addVolumeWallet(newWallet, o.getRemainingVolume());
                    //debita o valor na carteira do user dono da ordem de compra
                    userService.subDollarBalance(o.getUser(), o.getPrice()*o.getRemainingVolume());

                    orderService.subVolume(o, o.getRemainingVolume());

                }
                if (o.getPrice() >= createdOrder.getPrice() && o.getRemainingVolume() >= createdOrder.getRemainingVolume()) {

                    //atualiza o volume restante na ordem de compra
                    orderService.subVolume(o, createdOrder.getRemainingVolume());
                    //debita o volume na stockWallet do user dono da ordem de venda
                    stockBalanceService.subVolumeWallet(wallet, createdOrder.getRemainingVolume());
                    //credita o valor na carteira do user dono da ordem de venda
                    userService.addDollarBalance(user, o.getPrice()* createdOrder.getRemainingVolume());

                    //acha a carteida do user dono da ordem de compra
                    UserStockBalances newWallet =  stockBalanceService.findWallet(new UserStockBalancesId(o.getUser(),o.getId_stock())).orElse(new UserStockBalances(new UserStockBalancesId(o.getUser(),o.getId_stock()), o.getStock_symbol(), o.getStock_name(), o.getVolume()));
                    //acrescenta na stockBalance do user dono da ordem de compra
                    stockBalanceService.subVolumeWallet(newWallet, createdOrder.getRemainingVolume());
                    //debita o valor na carteira do user dono da ordem de compra
                    userService.subDollarBalance(o.getUser(), o.getPrice()* createdOrder.getRemainingVolume());

                    //atualiza o volume restante na ordem de venda
                    orderService.subVolume(createdOrder, createdOrder.getRemainingVolume());
                }
                //caso não reste volume a venda a ordem é desativada
                if (createdOrder.getRemainingVolume() == Long.valueOf(0)) {
                    //atualiza o status da ordem para inativa

                    orderService.disable(createdOrder);
                    break;
                }
            }
        }
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
}

    public ResponseEntity<UserOrders> buyDomain(CreateOrdersDTO order,User user, UserOrders createdOrder, UserStockBalances wallet, String token){
        //encontra todas as ordens abertas de compra para essa mesma stock
        List<UserOrders> orders = orderService.findAllSellOrders(order.getId_stock());

        //caso nao existam ordens de compra abertas para essa stock a ordem é salva.
        if (orders.isEmpty()) {
            stocksService.setMaxMinSell(order, token);
            orderService.save(createdOrder);
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } else {
            for (UserOrders o : orders) {

                //checa se essa ordem tem preço compativel com a ordem de compra
                if (o.getPrice() <= createdOrder.getPrice() && o.getRemainingVolume() <= createdOrder.getRemainingVolume()) {

                    //subtrai da stockBalance o user atual
                    stockBalanceService.addVolumeWallet(wallet, o.getRemainingVolume());
                    orderService.subVolume(createdOrder, o.getRemainingVolume());

                    //credita o valor na carteira
                    userService.subDollarBalance(user, createdOrder.getPrice()*o.getRemainingVolume());

                    //acha a carteida do user dono da ordem de compra
                    UserStockBalances newWallet =  stockBalanceService.findWallet(new UserStockBalancesId(o.getUser(),o.getId_stock())).orElse(new UserStockBalances(new UserStockBalancesId(o.getUser(),o.getId_stock()), o.getStock_symbol(), o.getStock_name(), o.getVolume()));
                    //acrescenta na stockBalance do user dono da ordem de compra
                    stockBalanceService.subVolumeWallet(newWallet, o.getRemainingVolume());
                    //debita o valor na carteira do user dono da ordem de compra
                    userService.addDollarBalance(o.getUser(), createdOrder.getPrice() * o.getRemainingVolume());
                    orderService.subVolume(o, o.getRemainingVolume());


                }
                if (o.getPrice() <= createdOrder.getPrice() && o.getRemainingVolume() >= createdOrder.getRemainingVolume()) {

                    //atualiza o volume restante na ordem de compra
                    orderService.subVolume(o, createdOrder.getRemainingVolume());
                    //debita o volume na stockWallet do user dono da ordem de venda
                    stockBalanceService.addVolumeWallet(wallet, createdOrder.getRemainingVolume());
                    //credita o valor na carteira do user dono da ordem de venda
                    userService.subDollarBalance(user, createdOrder.getPrice() * createdOrder.getRemainingVolume());

                    //acha a carteida do user dono da ordem de compra
                    UserStockBalances newWallet =  stockBalanceService.findWallet(new UserStockBalancesId(o.getUser(),o.getId_stock())).orElse(new UserStockBalances(new UserStockBalancesId(o.getUser(),o.getId_stock()), o.getStock_symbol(), o.getStock_name(), o.getVolume()));
                    //acrescenta na stockBalance do user dono da ordem de compra
                    stockBalanceService.subVolumeWallet(newWallet, createdOrder.getRemainingVolume());
                    //debita o valor na carteira do user dono da ordem de compra
                    userService.addDollarBalance(o.getUser(), createdOrder.getPrice()* createdOrder.getRemainingVolume());

                    //atualiza o volume restante na ordem de venda
                    orderService.subVolume( createdOrder, createdOrder.getRemainingVolume());
                }
                //caso não reste volume a venda a ordem é desativada
                if (createdOrder.getRemainingVolume() == Long.valueOf(0)) {
                    //atualiza o status da ordem para inativa

                    orderService.disable(createdOrder);
                    break;
                }
            }
        }
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/order/buy/prices/{stock}")
    public ResponseEntity<MaxMinDTO> getPricesBuy(@PathVariable(value = "stock") Long stock){
        return new ResponseEntity<>(orderService.findMaxMinBuyOrders(stock), HttpStatus.OK);
    }

    @GetMapping("/order/sell/prices/{stock}")
    public ResponseEntity<MaxMinDTO> getPricesSell(@PathVariable(value = "stock") Long stock){
        return new ResponseEntity<>(orderService.findMaxMinSellOrders(stock), HttpStatus.OK);
    }

    @PostMapping("/order/cancel")
    public ResponseEntity<?> cancelOrder(@Valid @RequestBody CancelOrdersDTO order){
        //cancela a ordem de Compra/Venda
        //resititui o volume ou dollar balance que ainda não foi utilizado
        return new ResponseEntity<>(HttpStatus.OK);
    }
}



//        Mono<StockDTO[]> stock =  this.webClient
//                .get()
//                .uri("/stocks")
//                .header(HttpHeaders.AUTHORIZATION, token)
//                .retrieve()
//                .bodyToMono(StockDTO[].class);
//        List lista = Arrays.stream(stock.block()).toList();
