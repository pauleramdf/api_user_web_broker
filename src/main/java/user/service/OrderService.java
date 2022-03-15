package user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import user.config.InvalidOrderException;
import user.dto.userOrders.FindAllOrdersByUserDTO;
import user.dto.stocks.StockPricesDTO;
import user.dto.userOrders.CreateOrdersDTO;
import user.dto.userOrders.MaxMinDto;
import user.dto.userOrders.UserOrdersDto;
import user.model.*;
import user.repository.UserOrdersMatchsRepository;
import user.repository.UserOrdersRepository;
import user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service("orderService")
public class OrderService {
    @Autowired
    private UserOrdersRepository ordersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockBalanceService stockBalanceService;

    @Autowired
    private UserService userService;

    @Autowired
    private StocksService stocksService;

    @Autowired
    private UserOrdersMatchsRepository matchsRepository;

    public List<UserOrders> findAllBuyOrders(Long stockName, Double price){
        return ordersRepository.findAllBuyOrders(stockName, price);
    }

    public List<UserOrders> findAllSellOrders(Long stockName, Double price){
        return ordersRepository.findAllSellOrders(stockName, price);
    }



    public void updateAllStatus(){
        List<UserOrders> orders = ordersRepository.findAllActive();
        for ( UserOrders order: orders
             ) {
                order.setStatus(0);
                ordersRepository.save(order);
        }
    }
    public List<UserOrders> getAllOrdersActives(){
        return ordersRepository.findAllActive();
    }
    public UserOrders save(UserOrders orders){
        return ordersRepository.save(orders);
    }

    public UserOrders subVolume(UserOrders order, Long volume){
        order.setRemainingVolume(order.getRemainingVolume() - volume);
        return ordersRepository.save(order);
    }

    public UserOrders addVolume(UserOrders order, Long volume){
        order.setVolume( order.getRemainingVolume() + volume);
        return ordersRepository.save(order);
    }

    public void disable(UserOrders order) {
        order.setStatus(0);
        ordersRepository.save(order);
    }

    public List<FindAllOrdersByUserDTO> findAllOrdersByUser(String username) {
        User user = userRepository.findByName(username).orElseThrow();
        return ordersRepository.findAllOrdersByUser(user.getId()).stream().map(u-> new FindAllOrdersByUserDTO(u)).toList();
    }

    public Optional<UserOrders> findById(Long id) {
        return ordersRepository.findById(id);
    }

    public Page<UserOrdersDto> findUserOrders(Pageable pageable, String username) {
        User user = userRepository.findByName(username).orElseThrow();
        Page<UserOrders> ordersPage = ordersRepository.findAllPaged(pageable, user.getId());
        return ordersPage.map(UserOrdersDto::new);
    }

    public UserOrders cancelOrder(UserOrders order, User user){

            this.disable(order);
            if(order.getType() == 1){
                //restituii sotckbalance
                UserStockBalances wallet = stockBalanceService.findWallet( new UserStockBalancesId(user, order.getIdStock())).orElseThrow();
                stockBalanceService.addVolumeWallet(wallet, order.getRemainingVolume());
            }
            else{
                //restitui o dinheiro
                Double restituicao = order.getRemainingVolume() * order.getPrice();
                userService.addDollarBalance(user, restituicao);

            }
            return order;
    }

    public void validateTransaction(User user, CreateOrdersDTO order) throws InvalidOrderException {


        if(order.getType() == 1 ){
            UserStockBalances wallet =  stockBalanceService.findWallet(new UserStockBalancesId(user, order.getIdStock())).orElseThrow();
            if(wallet.getVolume() < order.getVolume()){
                //invalid
                throw new InvalidOrderException("Usuario não possui saldo suficiente da stock");
            }
            stockBalanceService.subVolumeWallet(wallet, order.getVolume());
        }
        else{
            if (user.getDollarBalance() < order.getPrice()*order.getVolume()) {
                //invalid
                throw new InvalidOrderException("Usuario não possui saldo suficiente na carteira");
            }
            userService.subDollarBalance(user, order.getPrice()*order.getVolume());
        }

    }

    public ResponseEntity<UserOrders> buyDomain(UserOrders order, UserStockBalances wallet, String token){
        //encontra todas as ordens abertas de compra para essa mesma stock
        List<UserOrders> orders = this.findAllSellOrders(order.getIdStock(), order.getPrice());
        this.save(order);

        this.updateAskBid(order.getIdStock(), token);
        //caso nao existam ordens de compra abertas para essa stock a ordem é salva.
        if (orders.isEmpty()) {
            stockBalanceService.save(wallet);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } else {
            for (UserOrders o : orders) {
                //caso não reste volume a venda a ordem é desativada
                if (order.getRemainingVolume().equals(0L)) {
                    //atualiza o status da ordem para inativa
                    break;
                }
                //checa se essa ordem tem preço compativel com a ordem de compra
                if (o.getRemainingVolume() <= order.getRemainingVolume()) {
                    UserOrdersMatchs historic = new UserOrdersMatchs(new UserOrdersMatchsId(o,order));
                    matchsRepository.save(historic);

                    //subtrai da stockBalance o user atual
                    stockBalanceService.addVolumeWallet(wallet, o.getRemainingVolume());
                    this.subVolume(order, o.getRemainingVolume());

                    //debita o valor na carteira do user dono da ordem de compra
                    userService.addDollarBalance(o.getUser(), order.getPrice() * o.getRemainingVolume());
                    this.subVolume(o, o.getRemainingVolume());
                }
                if ( o.getRemainingVolume() >= order.getRemainingVolume()) {

                    UserOrdersMatchs historic = new UserOrdersMatchs(new UserOrdersMatchsId(o,order));
                    matchsRepository.save(historic);

                    //atualiza o volume restante na ordem de compra
                    this.subVolume(o, order.getRemainingVolume());
                    //debita o volume na stockWallet do user dono da ordem de venda
                    stockBalanceService.addVolumeWallet(wallet, order.getRemainingVolume());

                    //debita o valor na carteira do user dono da ordem de compra
                    userService.addDollarBalance(o.getUser(), order.getPrice()* order.getRemainingVolume());

                    //atualiza o volume restante na ordem de venda
                    this.subVolume( order, order.getRemainingVolume());
                }
            }
        }

        this.updateAllStatus();
        this.updateAskBid(order.getIdStock(), token);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    private void updateAskBid(Long idStock, String token) {
        System.out.println(idStock);
        MaxMinDto bid = this.findMaxMinOrders(idStock, 0);
        MaxMinDto ask = this.findMaxMinOrders(idStock, 1);

        StockPricesDTO stockPrices = new StockPricesDTO();
        stockPrices.setIdStock(idStock);
        stockPrices.setAskMax(ask.getMaxPrice());
        stockPrices.setAskMin(ask.getMinPrice());
        stockPrices.setBidMax(bid.getMaxPrice());
        stockPrices.setBidMin(bid.getMinPrice());

        stocksService.updateAskBid(stockPrices, token);
    }
    public MaxMinDto findMaxMinOrders(Long stock, Integer type){
        return ordersRepository.findMaxMinOrders(stock, type).orElse(new MaxMinDto(null,null));
    }

    public ResponseEntity<UserOrders> sellDomain(User user, UserOrders order, String token){

        //encontra todas as ordens abertas de compra para essa mesma stock
        List<UserOrders> orders = this.findAllBuyOrders(order.getIdStock(), order.getPrice());
        this.save(order);
        this.updateAskBid(order.getIdStock(), token);

        //caso nao existam ordens de compra abertas para essa stock a ordem é salva.
        if (orders.isEmpty()) {
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } else {
            for (UserOrders o : orders) {
                if (order.getRemainingVolume().equals(0L)) {
                    //atualiza o status da ordem para inativa
                    break;
                }
                //checa se essa ordem tem preço compativel com a ordem de venda
                if (o.getRemainingVolume() <= order.getRemainingVolume()) {

                    UserOrdersMatchs historic = new UserOrdersMatchs(new UserOrdersMatchsId(order,o));
                    matchsRepository.save(historic);
                    //subtrai o volume da ordem de venda
                    this.subVolume(order, o.getRemainingVolume());

                    //credita o valor na carteira
                    userService.addDollarBalance(user, o.getPrice()* o.getRemainingVolume());

                    //acha a carteida do user dono da ordem de compra
                    UserStockBalances newWallet =  stockBalanceService.findWallet(new UserStockBalancesId(o.getUser(),o.getIdStock())).orElseThrow();
                    //acrescenta na stockBalance do user dono da ordem de compra
                    stockBalanceService.addVolumeWallet(newWallet, o.getRemainingVolume());

                    this.subVolume(o, o.getRemainingVolume());



                }
                if (o.getRemainingVolume() >= order.getRemainingVolume() ) {

                    UserOrdersMatchs historic = new UserOrdersMatchs(new UserOrdersMatchsId(order,o));
                    matchsRepository.save(historic);

                    //atualiza o volume restante na ordem de compra
                    this.subVolume(o, order.getRemainingVolume());

                    //credita o valor na carteira do user dono da ordem de venda
                    userService.addDollarBalance(user, o.getPrice()* order.getRemainingVolume());

                    //acha a carteida do user dono da ordem de compra
                    UserStockBalances newWallet =  stockBalanceService.findWallet(new UserStockBalancesId(o.getUser(),o.getIdStock())).orElseThrow();
                    //acrescenta na stockBalance do user dono da ordem de compra
                    stockBalanceService.subVolumeWallet(newWallet, order.getRemainingVolume());

                    //atualiza o volume restante na ordem de venda
                    this.subVolume(order, order.getRemainingVolume());
                }
                //caso não reste volume a venda a ordem é desativada
            }
        }
        this.updateAllStatus();
        this.updateAskBid(order.getIdStock(), token);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }


}
