package user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.dto.CancelOrdersDTO;
import user.dto.PageDto;
import user.dto.userOrders.CreateOrdersDTO;
import user.dto.MaxMinDTO;
import user.dto.userOrders.UserOrdersDto;
import user.model.*;
import user.repository.UserOrdersMatchsRepository;
import user.service.OrderService;
import user.service.StockBalanceService;
import user.service.StocksService;
import user.service.UserService;

import javax.validation.Valid;
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

    @Autowired
    private UserOrdersMatchsRepository matchsRepository;

    @PostMapping("/order")
    public ResponseEntity<UserOrders> createOrder( @RequestBody @Valid CreateOrdersDTO order, @RequestHeader("Authorization") String token) {

        //checa se o user existe de fato
        User user = userService.findByName(order.getUsername()).orElseThrow(Error::new);
        //valida a transação
        validateTransaction(user, order);

        //cria ordem
        UserOrders createdOrder = order.transformaParaObjeto(user);

        //pega a carteira existente ou caso não exista cria uma zerada
        UserStockBalances wallet
                = stockBalanceService.findById(new UserStockBalancesId(user, order.getId_stock())).orElse(new UserStockBalances(new UserStockBalancesId(user, order.getId_stock()), order.getStock_symbol(), order.getStock_name(),0L));

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
            stockBalanceService.subVolumeWallet(wallet, order.getVolume());
        }
        else{
            if (user.getDollar_balance() < order.getPrice()*order.getVolume()) {
                //invalid
                throw new RuntimeException();
            }
            userService.subDollarBalance(user, order.getPrice()*order.getVolume());
        }
        return;
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

                    UserOrdersMatchs historic = new UserOrdersMatchs(new UserOrdersMatchsId(order,o));
                    matchsRepository.save(historic);
                    //subtrai o volume da ordem de venda
                    orderService.subVolume(order, o.getRemainingVolume());

                    //credita o valor na carteira
                    userService.addDollarBalance(user, o.getPrice()* o.getRemainingVolume());

                    //acha a carteida do user dono da ordem de compra
                    UserStockBalances newWallet =  stockBalanceService.findWallet(new UserStockBalancesId(o.getUser(),o.getId_stock())).orElseThrow();
                    //acrescenta na stockBalance do user dono da ordem de compra
                    stockBalanceService.addVolumeWallet(newWallet, o.getRemainingVolume());

                    orderService.subVolume(o, o.getRemainingVolume());



                }
                if (o.getRemainingVolume() >= order.getRemainingVolume() ) {

                    UserOrdersMatchs historic = new UserOrdersMatchs(new UserOrdersMatchsId(order,o));
                    matchsRepository.save(historic);

                    //atualiza o volume restante na ordem de compra
                    orderService.subVolume(o, order.getRemainingVolume());

                    //credita o valor na carteira do user dono da ordem de venda
                    userService.addDollarBalance(user, o.getPrice()* order.getRemainingVolume());

                    //acha a carteida do user dono da ordem de compra
                    UserStockBalances newWallet =  stockBalanceService.findWallet(new UserStockBalancesId(o.getUser(),o.getId_stock())).orElseThrow();
                    //acrescenta na stockBalance do user dono da ordem de compra
                    stockBalanceService.subVolumeWallet(newWallet, order.getRemainingVolume());

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
            stockBalanceService.save(wallet);
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
                    UserOrdersMatchs historic = new UserOrdersMatchs(new UserOrdersMatchsId(o,order));
                    matchsRepository.save(historic);

                    //subtrai da stockBalance o user atual
                    stockBalanceService.addVolumeWallet(wallet, o.getRemainingVolume());
                    orderService.subVolume(order, o.getRemainingVolume());

                    //debita o valor na carteira do user dono da ordem de compra
                    userService.addDollarBalance(o.getUser(), order.getPrice() * o.getRemainingVolume());
                    orderService.subVolume(o, o.getRemainingVolume());
                }
                if ( o.getRemainingVolume() >= order.getRemainingVolume()) {

                    UserOrdersMatchs historic = new UserOrdersMatchs(new UserOrdersMatchsId(o,order));
                    matchsRepository.save(historic);

                    //atualiza o volume restante na ordem de compra
                    orderService.subVolume(o, order.getRemainingVolume());
                    //debita o volume na stockWallet do user dono da ordem de venda
                    stockBalanceService.addVolumeWallet(wallet, order.getRemainingVolume());

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
    public ResponseEntity<?> cancelOrder(@Valid @RequestBody CancelOrdersDTO orderdto){
        //cancela a ordem de Compra/Venda
        //resititui o volume ou dollar balance que ainda não foi utilizado

        User user = userService.findByName(orderdto.getUser_name()).orElseThrow();
        UserOrders order = orderService.findById(orderdto.getId()).orElseThrow();

        if(order.getStatus() == 1){
            orderService.disable(order);
            if(order.getType() == 1){
                //restituii sotckbalance
                UserStockBalances wallet = stockBalanceService.findWallet( new UserStockBalancesId(user,orderdto.getId_stock())).orElseThrow();
                stockBalanceService.addVolumeWallet(wallet, order.getRemainingVolume());
            }
            else{
                //restitui o dinheiro
                Double restituicao = order.getRemainingVolume() * order.getPrice();
                userService.addDollarBalance(user, restituicao);

            }
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("order/paged")
    public ResponseEntity<Page<UserOrdersDto>> getPage(@RequestBody PageDto page) {
        try {
            Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
            return ResponseEntity.ok().body(orderService.findUserOrders(pageable, page.getUsername()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/order/matchs/buy/{id}")
    public ResponseEntity<?> gethistoryBuy(@PathVariable(value = "id") Long orderId){
        return new ResponseEntity<>(matchsRepository.getMatchBuyHistory(orderId), HttpStatus.OK);
    }

    @GetMapping("/order/matchs/sell/{id}")
    public ResponseEntity<?> gethistorySell(@PathVariable(value = "id") Long orderId){
        return new ResponseEntity<>(matchsRepository.getMatchSellHistory(orderId), HttpStatus.OK);
    }

}
