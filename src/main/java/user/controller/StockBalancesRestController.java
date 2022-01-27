package user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.dto.CreateStockBalanceDTO;
import user.dto.FindAllByUserDTO;
import user.model.User;
import user.model.UserStockBalances;
import user.repository.UserRepository;
import user.service.StockBalanceService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@ComponentScan("com.user.repository")
@RequestMapping()
public class StockBalancesRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockBalanceService stockBalanceService;

    @GetMapping("stockbalances/{id}")
    public ResponseEntity<?> getStockBalances(@Valid @PathVariable(value = "id") Long id){
        List<FindAllByUserDTO> wallet =  stockBalanceService.findAllByUser(id);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @PostMapping("/create/balance")
    public ResponseEntity<UserStockBalances> createStockBalance(@Valid @RequestBody CreateStockBalanceDTO stockBalance){

        User user = userRepository.findByName(stockBalance.getUsername()).orElseThrow();
        UserStockBalances wallet =  stockBalanceService.save(stockBalance.transformaDTO(user));
        return new ResponseEntity<>(wallet, HttpStatus.CREATED);
    }
}
