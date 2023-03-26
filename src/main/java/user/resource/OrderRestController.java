package user.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.config.ApiUserDefaultException;
import user.dto.userorders.*;
import user.model.*;
import user.repository.UserOrdersMatchsRepository;
import user.service.OrderService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService orderService;
    private final UserOrdersMatchsRepository matchsRepository;

    @PostMapping()
    public ResponseEntity<UserOrders> createOrder(@RequestBody @Valid UserOrdersDto order, @RequestHeader("Authorization") String token)  throws ApiUserDefaultException {
            return new ResponseEntity<>(orderService.createOrder(order, token), HttpStatus.CREATED);

    }

    @GetMapping()
    public ResponseEntity<List<FindAllOrdersByUserDTO>> getOrders() {
        return new ResponseEntity<>(orderService.findAllOrdersByUser(), HttpStatus.OK);
    }

    @PostMapping("/cancel")
    public ResponseEntity<UserOrders> cancelOrder(@Valid @RequestBody CancelOrdersDTO orderdto) {
        //cancela a ordem de Compra/Venda
        //resititui o volume ou dollar balance que ainda não foi utilizado
        return new ResponseEntity<>(orderService.cancelOrder(orderdto), HttpStatus.OK);
    }

    @PostMapping("/paged")
    public ResponseEntity<Page<UserOrdersDto>> getPage(Pageable pageable) {
        try {
            return ResponseEntity.ok().body(orderService.findUserOrders(pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/matchs/buy/{id}")
    public ResponseEntity<List<UserOrdersMatchs>> gethistoryBuy(@PathVariable(value = "id") Long orderId) {
        return new ResponseEntity<>(matchsRepository.getMatchBuyHistory(orderId), HttpStatus.OK);
    }

    @GetMapping("/matchs/sell/{id}")
    public ResponseEntity<List<UserOrdersMatchs>> gethistorySell(@PathVariable(value = "id") Long orderId) {
        return new ResponseEntity<>(matchsRepository.getMatchSellHistory(orderId), HttpStatus.OK);
    }

}
