package user.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Order")
public class OrderRestController {

    private final OrderService orderService;
    private final UserOrdersMatchsRepository matchsRepository;

    @Operation(summary = "Create Order",
            description = ".Ex.: '/order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully created"),
                    @ApiResponse(responseCode = "404", description = "Error interno", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping()
    public ResponseEntity<UserOrders> createOrder(@RequestBody @Valid UserOrdersDto order, @RequestHeader("Authorization") String token)  throws ApiUserDefaultException {
            return new ResponseEntity<>(orderService.createOrder(order, token), HttpStatus.CREATED);

    }

    @GetMapping()
    public ResponseEntity<List<FindAllOrdersByUserDTO>> getOrders() {
        return new ResponseEntity<>(orderService.findAllOrdersByUser(), HttpStatus.OK);
    }

    @Operation(summary = "Cancel Order",
            description = ".Ex.: '/order/cancel",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully canceled"),
                    @ApiResponse(responseCode = "404", description = "Error interno", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping("/cancel")
    public ResponseEntity<UserOrders> cancelOrder(@Valid @RequestBody CancelOrdersDTO orderdto) {
        return new ResponseEntity<>(orderService.cancelOrder(orderdto), HttpStatus.OK);
    }

    @PostMapping("/paged")
    public ResponseEntity<Page<UserOrdersDto>> getPage(Pageable pageable) {
        return ResponseEntity.ok().body(orderService.findUserOrders(pageable));
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
