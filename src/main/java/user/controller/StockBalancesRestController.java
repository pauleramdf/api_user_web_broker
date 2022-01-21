package user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import user.DTO.CreateStockBalanceDTO;
import user.DTO.FindAllByUserDTO;
import user.DTO.GetStockBalancesDTO;
import user.model.User;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;
import user.repository.UserOrdersRepository;
import user.repository.UserRepository;
import user.repository.UserStockBalancesRepository;
import user.service.StockBalanceService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@ComponentScan("com.user.repository")
@RequestMapping(consumes = "application/json")
public class StockBalancesRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockBalanceService stockBalanceService;

    @GetMapping("stockbalances/{id}")
    public ResponseEntity<List<FindAllByUserDTO>> getStockBalances(@Valid @PathVariable(value = "id") Long id){
        List<FindAllByUserDTO> wallet =  stockBalanceService.findAllByUser(id);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @PostMapping("/create/balance")
    public ResponseEntity<UserStockBalances> createStockBalance(@Valid @RequestBody CreateStockBalanceDTO stockBalance){
        User user = userRepository.findById(stockBalance.getId_user()).orElseThrow();
        UserStockBalances wallet =  stockBalanceService.save(stockBalance.transformaDTO(user));
        UserStockBalances u = new UserStockBalances();
        return new ResponseEntity<>(wallet, HttpStatus.CREATED);
    }
}
