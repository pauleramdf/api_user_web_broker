package user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import user.config.ApiUserDefaultException;
import user.dto.userorders.CancelOrdersDTO;
import user.dto.userorders.FindAllOrdersByUserDTO;
import user.dto.stocks.StockPricesDTO;
import user.dto.userorders.MaxMinDto;
import user.dto.userorders.UserOrdersDto;
import user.model.*;
import user.repository.UserOrdersMatchsRepository;
import user.repository.UserOrdersRepository;

import java.util.List;

@RequiredArgsConstructor
@Service("orderService")
public class OrderService {
    private final UserOrdersRepository ordersRepository;
    private final StockBalanceService stockBalanceService;
    private final UserService userService;
    private final StocksService stocksService;
    private final UserOrdersMatchsRepository matchsRepository;

    public List<UserOrders> findAllBuyOrders(Long stockName, Double price) {
        return ordersRepository.findAllBuyOrders(stockName, price);
    }

    public List<UserOrders> findAllSellOrders(Long stockName, Double price) {
        return ordersRepository.findAllSellOrders(stockName, price);
    }


    public void updateAllStatus() {
        List<UserOrders> orders = ordersRepository.findAllActive();
        for (UserOrders order : orders
        ) {
            order.setStatus(0);
            ordersRepository.save(order);
        }
    }

    public List<UserOrders> getAllOrdersActives() {
        return ordersRepository.findAllActive();
    }

    public UserOrders save(UserOrders orders) {
        return ordersRepository.save(orders);
    }

    public UserOrders subVolume(UserOrders order, Long volume) {
        order.setRemainingVolume(order.getRemainingVolume() - volume);
        return ordersRepository.save(order);
    }

    public UserOrders addVolume(UserOrders order, Long volume) {
        order.setVolume(order.getRemainingVolume() + volume);
        return ordersRepository.save(order);
    }

    public void disable(UserOrders order) {
        order.setStatus(0);
        ordersRepository.save(order);
    }

    public List<FindAllOrdersByUserDTO> findAllOrdersByUser() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByName(principal.getUsername()).orElseThrow();
        return ordersRepository.findAllOrdersByUser(user.getId()).stream().map(FindAllOrdersByUserDTO::new).toList();
    }

    public Page<UserOrdersDto> findUserOrders(Pageable pageable) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.findByName(principal.getUsername()).orElseThrow();
        Page<UserOrders> ordersPage = ordersRepository.findAllPaged(pageable, user.getId());
        return ordersPage.map(UserOrdersDto::new);
    }

    public UserOrders cancelOrder(CancelOrdersDTO cancelOrdersDTO) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByName(principal.getUsername()).orElseThrow();
        UserOrders order = ordersRepository.findById(cancelOrdersDTO.getId()).orElseThrow();

        this.disable(order);
        if (order.getType() == 1) {
            //restituii sotckbalance
            UserStockBalances wallet = stockBalanceService.findWallet(new UserStockBalancesId(user, order.getIdStock())).orElseThrow();
            stockBalanceService.addVolumeWallet(wallet, order.getRemainingVolume());
        } else {
            //restitui o dinheiro
            Double restituicao = order.getRemainingVolume() * order.getPrice();
            userService.addDollarBalance(user, restituicao);

        }
        return order;
    }

    public void validateTransaction(User user, UserOrdersDto order) throws ApiUserDefaultException {


        if (order.getType() == 1) {
            UserStockBalances wallet = stockBalanceService.findWallet(new UserStockBalancesId(user, order.getIdStock())).orElseThrow();
            if (wallet.getVolume() < order.getVolume()) {
                //invalid
                //throw new InvalidOrderException("Usuario não possui saldo suficiente da stock");
            }
            stockBalanceService.subVolumeWallet(wallet, order.getVolume());
        } else {
            if (user.getDollarBalance() < order.getPrice() * order.getVolume()) {
                //invalid
                throw new ApiUserDefaultException("Usuario não possui saldo suficiente na carteira");
            }
            userService.subDollarBalance(user, order.getPrice() * order.getVolume());
        }

    }

    public UserOrders buyDomain(UserOrders order, UserStockBalances wallet, String token) {
        //encontra todas as ordens abertas de compra para essa mesma stock
        List<UserOrders> orders = this.findAllSellOrders(order.getIdStock(), order.getPrice());
        this.save(order);

        this.updateAskBid(order.getIdStock(), token);
        //caso nao existam ordens de compra abertas para essa stock a ordem é salva.
        if (orders.isEmpty()) {
            stockBalanceService.save(wallet);
            return order;
        } else {
            for (UserOrders o : orders) {
                //caso não reste volume a venda a ordem é desativada
                if (order.getRemainingVolume().equals(0L)) {
                    //atualiza o status da ordem para inativa
                    break;
                }
                //checa se essa ordem tem preço compativel com a ordem de compra
                if (o.getRemainingVolume() <= order.getRemainingVolume()) {
                    UserOrdersMatchs historic = new UserOrdersMatchs(new UserOrdersMatchsId(o, order));
                    matchsRepository.save(historic);

                    //subtrai da stockBalance o user atual
                    stockBalanceService.addVolumeWallet(wallet, o.getRemainingVolume());
                    this.subVolume(order, o.getRemainingVolume());

                    //debita o valor na carteira do user dono da ordem de compra
                    userService.addDollarBalance(o.getUser(), order.getPrice() * o.getRemainingVolume());
                    this.subVolume(o, o.getRemainingVolume());
                }
                if (o.getRemainingVolume() >= order.getRemainingVolume()) {

                    UserOrdersMatchs historic = new UserOrdersMatchs(new UserOrdersMatchsId(o, order));
                    matchsRepository.save(historic);

                    //atualiza o volume restante na ordem de compra
                    this.subVolume(o, order.getRemainingVolume());
                    //debita o volume na stockWallet do user dono da ordem de venda
                    stockBalanceService.addVolumeWallet(wallet, order.getRemainingVolume());

                    //debita o valor na carteira do user dono da ordem de compra
                    userService.addDollarBalance(o.getUser(), order.getPrice() * order.getRemainingVolume());

                    //atualiza o volume restante na ordem de venda
                    this.subVolume(order, order.getRemainingVolume());
                }
            }
        }

        this.updateAllStatus();
        this.updateAskBid(order.getIdStock(), token);
        return order;
    }

    private void updateAskBid(Long idStock, String token) {
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

    public MaxMinDto findMaxMinOrders(Long stock, Integer type) {
        return ordersRepository.findMaxMinOrders(stock, type).orElse(new MaxMinDto(null, null));
    }

    public UserOrders sellDomain(User user, UserOrders order, String token) {

        //encontra todas as ordens abertas de compra para essa mesma stock
        List<UserOrders> orders = this.findAllBuyOrders(order.getIdStock(), order.getPrice());
        this.save(order);
        this.updateAskBid(order.getIdStock(), token);

        //caso nao existam ordens de compra abertas para essa stock a ordem é salva.
        if (orders.isEmpty()) {
            return order;
        } else {
            for (UserOrders o : orders) {
                if (order.getRemainingVolume().equals(0L)) {
                    //atualiza o status da ordem para inativa
                    break;
                }
                //checa se essa ordem tem preço compativel com a ordem de venda
                if (o.getRemainingVolume() <= order.getRemainingVolume()) {

                    UserOrdersMatchs historic = new UserOrdersMatchs(new UserOrdersMatchsId(order, o));
                    matchsRepository.save(historic);
                    //subtrai o volume da ordem de venda
                    this.subVolume(order, o.getRemainingVolume());

                    //credita o valor na carteira
                    userService.addDollarBalance(user, o.getPrice() * o.getRemainingVolume());

                    //acha a carteida do user dono da ordem de compra
                    UserStockBalances newWallet = stockBalanceService.findWallet(new UserStockBalancesId(o.getUser(), o.getIdStock())).orElseThrow();
                    //acrescenta na stockBalance do user dono da ordem de compra
                    stockBalanceService.addVolumeWallet(newWallet, o.getRemainingVolume());

                    this.subVolume(o, o.getRemainingVolume());


                }
                if (o.getRemainingVolume() >= order.getRemainingVolume()) {

                    UserOrdersMatchs historic = new UserOrdersMatchs(new UserOrdersMatchsId(order, o));
                    matchsRepository.save(historic);

                    //atualiza o volume restante na ordem de compra
                    this.subVolume(o, order.getRemainingVolume());

                    //credita o valor na carteira do user dono da ordem de venda
                    userService.addDollarBalance(user, o.getPrice() * order.getRemainingVolume());

                    //acha a carteida do user dono da ordem de compra
                    UserStockBalances newWallet = stockBalanceService.findWallet(new UserStockBalancesId(o.getUser(), o.getIdStock())).orElseThrow();
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
        return order;
    }


    public UserOrders createOrder(UserOrdersDto order, String token) throws ApiUserDefaultException {

        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //checa se o user existe de fato
        User user = userService.findByName(principal.getUsername()).orElseThrow(Error::new);
        //valida a transação
        validateTransaction(user, order);

        //cria ordem
        UserOrders createdOrder = order.transformaParaObjeto(user);

        //pega a carteira existente ou caso não exista cria uma zerada
        UserStockBalances wallet
                = stockBalanceService.findById(new UserStockBalancesId(user, order.getIdStock())).orElse(new UserStockBalances(new UserStockBalancesId(user, order.getIdStock()), order.getStockSymbol(), order.getStockName(), 0L));

        if (order.getType() == 1) {
            return sellDomain(user, createdOrder, token);
        } else {
            return buyDomain(createdOrder, wallet, token);
        }
    }
}
