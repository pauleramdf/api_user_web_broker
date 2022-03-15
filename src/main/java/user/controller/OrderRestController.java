package user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.dto.userorders.*;
import user.model.*;
import user.repository.UserOrdersMatchsRepository;
import user.service.OrderService;
import user.service.StockBalanceService;
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
    private UserService userService;

    @Autowired
    private UserOrdersMatchsRepository matchsRepository;

    @PostMapping("/order")
    public ResponseEntity<UserOrders> createOrder( @RequestBody @Valid CreateOrdersDTO order, @RequestHeader("Authorization") String token, Principal userJWT) {
        try{
        //checa se o user existe de fato
        User user = userService.findByName(userJWT.getName()).orElseThrow(Error::new);
        //valida a transação
        orderService.validateTransaction(user, order);

        //cria ordem
        UserOrders createdOrder = order.transformaParaObjeto(user);

        //pega a carteira existente ou caso não exista cria uma zerada
        UserStockBalances wallet
                = stockBalanceService.findById(new UserStockBalancesId(user, order.getIdStock())).orElse(new UserStockBalances(new UserStockBalancesId(user, order.getIdStock()), order.getStockSymbol(), order.getStockName(),0L));

        if (order.getType() == 1) {
            return orderService.sellDomain(user, createdOrder, token);
        }

        else {
            return orderService.buyDomain(createdOrder, wallet, token);
        }}
        catch (Exception e){
            System.out.println(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/order")
    public ResponseEntity<List<FindAllOrdersByUserDTO>> getOrders(Principal userJWT){
        return new ResponseEntity<>(orderService.findAllOrdersByUser(userJWT.getName()), HttpStatus.OK);
    }

    @PostMapping("/order/cancel")
    public ResponseEntity< UserOrders> cancelOrder(@Valid @RequestBody CancelOrdersDTO orderdto, Principal userJWT){
        //cancela a ordem de Compra/Venda
        //resititui o volume ou dollar balance que ainda não foi utilizado

        try{
            User user = userService.findByName(userJWT.getName()).orElseThrow();
            UserOrders order = orderService.findById(orderdto.getId()).orElseThrow();

            return new ResponseEntity<>(orderService.cancelOrder(order, user), HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

    }

    @PostMapping("order/paged")
    public ResponseEntity<Page<UserOrdersDto>> getPage(@RequestBody OrderPageDto page, Principal userJWT) {
        try {
            Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
            return ResponseEntity.ok().body(orderService.findUserOrders(pageable, userJWT.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/order/matchs/buy/{id}")
    public ResponseEntity<List<UserOrdersMatchs> > gethistoryBuy(@PathVariable(value = "id") Long orderId){
        return new ResponseEntity<>(matchsRepository.getMatchBuyHistory(orderId), HttpStatus.OK);
    }

    @GetMapping("/order/matchs/sell/{id}")
    public ResponseEntity<List<UserOrdersMatchs> > gethistorySell(@PathVariable(value = "id") Long orderId){
        return new ResponseEntity<>(matchsRepository.getMatchSellHistory(orderId), HttpStatus.OK);
    }

}
