package user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import user.DTO.CreateStockBalanceDTO;
import user.model.User;
import user.model.UserStockBalances;
import user.repository.UserOrdersRepository;
import user.repository.UserRepository;
import user.repository.UserStockBalancesRepository;

import javax.validation.Valid;

@RestController
@CrossOrigin
@ComponentScan("com.user.repository")
public class StockBalancesRestController {

    @Autowired
    private WebClient webClient;

    @Autowired
    private UserOrdersRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStockBalancesRepository walletRepository;

    @PostMapping("/create/balance")
    public ResponseEntity<UserStockBalances> createStockBalance(@Valid @RequestBody CreateStockBalanceDTO stockBalance){
        User user = userRepository.findById(stockBalance.getId_user()).orElseThrow();
        UserStockBalances wallet =  walletRepository.save(stockBalance.transformaDTO(user));
        return new ResponseEntity<>(wallet, HttpStatus.CREATED);
    }
}
