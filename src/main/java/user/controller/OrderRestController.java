package user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.dto.CancelOrdersDTO;
import user.dto.CreateOrdersDTO;
import user.dto.MaxMinDTO;
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
@RequestMapping
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
        User user = userService.findByName(order.getUsername()).orElseThrow(Error::new);
        //valida a transação
        validateTransaction(user, order);

        //cria ordem
        UserOrders createdOrder = order.transformaParaObjeto(user);

        //pega a carteira existente ou caso não exista cria uma zerada
        UserStockBalances wallet
                = stockBalanceService.findById(new UserStockBalancesId(user, order.getId_stock())).orElse(new UserStockBalances(new UserStockBalancesId(user, order.getId_stock()), order.getStock_symbol(), order.getStock_name(), Long.valueOf(0)));

        if (order.getType() == 1) {
            return sellDomain(user, createdOrder, wallet, token);
        }

        else {
            return buyDomain(user, createdOrder, wallet, token);
        }
    }

    private void validateTransaction(User user, CreateOrdersDTO order) {


        if(order.getType() == 1 ){
            UserStockBalances wallet =  stockBalanceService.findWallet(new UserStockBalancesId(user, order.getId_stock())).orElseThrow();
            if(wallet.getVolume() < order.getVolume()){
                //invalid
                throw new RuntimeException();
            }
           return;
        }
        else{
            if (user.getDollar_balance() < order.getPrice()*order.getVolume()) {
                //invalid
                throw new RuntimeException();
            }
            return;
        }
    }

    public ResponseEntity<UserOrders> sellDomain(User user, UserOrders order, UserStockBalances wallet, String token){

        //encontra todas as ordens abertas de compra para essa mesma stock
        List<UserOrders> orders = orderService.findAllBuyOrders(order.getId_stock(), order.getPrice());
        orderService.save(order);
        stocksService.updateAskBid(order.getId_stock(), token);

        //caso nao existam ordens de compra abertas para essa stock a ordem é salva.
        if (orders.isEmpty()) {
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } else {
            stocksService.updateAskBid(order.getId_stock(), token);
            for (UserOrders o : orders) {
                if (order.getRemainingVolume() == Long.valueOf(0)) {
                    //atualiza o status da ordem para inativa
                    break;
                }
                //checa se essa ordem tem preço compativel com a ordem de venda
                if (o.getRemainingVolume() <= order.getRemainingVolume()) {

                    //subtrai da stockBalance o user atual
                    stockBalanceService.subVolumeWallet(wallet, o.getRemainingVolume());
                    //subtrai o volume da ordem de venda
                    orderService.subVolume(order, o.getRemainingVolume());

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
                if (o.getRemainingVolume() >= order.getRemainingVolume() ) {

                    //atualiza o volume restante na ordem de compra
                    orderService.subVolume(o, order.getRemainingVolume());
                    //debita o volume na stockWallet do user dono da ordem de venda
                    stockBalanceService.subVolumeWallet(wallet, order.getRemainingVolume());
                    //credita o valor na carteira do user dono da ordem de venda
                    userService.addDollarBalance(user, o.getPrice()* order.getRemainingVolume());

                    //acha a carteida do user dono da ordem de compra
                    UserStockBalances newWallet =  stockBalanceService.findWallet(new UserStockBalancesId(o.getUser(),o.getId_stock())).orElse(new UserStockBalances(new UserStockBalancesId(o.getUser(),o.getId_stock()), o.getStock_symbol(), o.getStock_name(), o.getVolume()));
                    //acrescenta na stockBalance do user dono da ordem de compra
                    stockBalanceService.subVolumeWallet(newWallet, order.getRemainingVolume());
                    //debita o valor na carteira do user dono da ordem de compra
                    userService.subDollarBalance(o.getUser(), o.getPrice()* order.getRemainingVolume());

                    //atualiza o volume restante na ordem de venda
                    orderService.subVolume(order, order.getRemainingVolume());
                }
                //caso não reste volume a venda a ordem é desativada
            }
        }
        orderService.updateAllStatus();
        stocksService.updateAskBid(order.getId_stock(), token);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
}

    public ResponseEntity<UserOrders> buyDomain(User user, UserOrders order, UserStockBalances wallet, String token){
        //encontra todas as ordens abertas de compra para essa mesma stock
        List<UserOrders> orders = orderService.findAllSellOrders(order.getId_stock(), order.getPrice());
        orderService.save(order);
        stocksService.updateAskBid(order.getId_stock(), token);
        //caso nao existam ordens de compra abertas para essa stock a ordem é salva.
        if (orders.isEmpty()) {
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } else {
            for (UserOrders o : orders) {
                //caso não reste volume a venda a ordem é desativada
                if (order.getRemainingVolume() == Long.valueOf(0)) {
                    //atualiza o status da ordem para inativa
                    break;
                }
                //checa se essa ordem tem preço compativel com a ordem de compra
                if (o.getRemainingVolume() <= order.getRemainingVolume()) {

                    //subtrai da stockBalance o user atual
                    stockBalanceService.addVolumeWallet(wallet, o.getRemainingVolume());
                    orderService.subVolume(order, o.getRemainingVolume());

                    //credita o valor na carteira
                    userService.subDollarBalance(user, order.getPrice()*o.getRemainingVolume());

                    //acha a carteida do user dono da ordem de compra
                    UserStockBalances newWallet =  stockBalanceService.findWallet(new UserStockBalancesId(o.getUser(),o.getId_stock())).orElse(new UserStockBalances(new UserStockBalancesId(o.getUser(),o.getId_stock()), o.getStock_symbol(), o.getStock_name(), o.getVolume()));
                    //acrescenta na stockBalance do user dono da ordem de compra
                    stockBalanceService.subVolumeWallet(newWallet, o.getRemainingVolume());
                    //debita o valor na carteira do user dono da ordem de compra
                    userService.addDollarBalance(o.getUser(), order.getPrice() * o.getRemainingVolume());
                    orderService.subVolume(o, o.getRemainingVolume());
                }
                if ( o.getRemainingVolume() >= order.getRemainingVolume()) {

                    //atualiza o volume restante na ordem de compra
                    orderService.subVolume(o, order.getRemainingVolume());
                    //debita o volume na stockWallet do user dono da ordem de venda
                    stockBalanceService.addVolumeWallet(wallet, order.getRemainingVolume());
                    //credita o valor na carteira do user dono da ordem de venda
                    userService.subDollarBalance(user, order.getPrice() * order.getRemainingVolume());

                    //acha a carteida do user dono da ordem de compra
                    UserStockBalances newWallet =  stockBalanceService.findWallet(new UserStockBalancesId(o.getUser(),o.getId_stock())).orElse(new UserStockBalances(new UserStockBalancesId(o.getUser(),o.getId_stock()), o.getStock_symbol(), o.getStock_name(), o.getVolume()));
                    //acrescenta na stockBalance do user dono da ordem de compra
                    stockBalanceService.subVolumeWallet(newWallet, order.getRemainingVolume());
                    //debita o valor na carteira do user dono da ordem de compra
                    userService.addDollarBalance(o.getUser(), order.getPrice()* order.getRemainingVolume());

                    //atualiza o volume restante na ordem de venda
                    orderService.subVolume( order, order.getRemainingVolume());
                }
            }
        }

        orderService.updateAllStatus();
        stocksService.updateAskBid(order.getId_stock(), token);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/order/buy/prices/{stock}")
    public ResponseEntity<MaxMinDTO> getPricesBuy(@PathVariable(value = "stock") Long stock){
        return new ResponseEntity<>(orderService.findMaxMinBuyOrders(stock), HttpStatus.OK);
    }

    @GetMapping("/order/sell/prices/{stock}")
    public ResponseEntity<MaxMinDTO> getPricesSell(@PathVariable(value = "stock") Long stock){
        return new ResponseEntity<>(orderService.findMaxMinSellOrders(stock), HttpStatus.OK);
    }

    @GetMapping("/orders/{username}")
    public ResponseEntity<?> getOrders(@PathVariable(value= "username") String username){
        return new ResponseEntity<>(orderService.FindAllOrdersByUser(username), HttpStatus.OK);
    }

    @PostMapping("/order/cancel")
    public ResponseEntity<?> cancelOrder(@Valid @RequestBody CancelOrdersDTO order){
        //cancela a ordem de Compra/Venda
        //resititui o volume ou dollar balance que ainda não foi utilizado
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
