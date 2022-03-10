package user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.dto.userStockBalances.CreateStockBalanceDTO;
import user.dto.userStockBalances.FindAllByUserDTO;
import user.model.User;
import user.model.UserStockBalances;
import user.repository.UserRepository;
import user.service.StockBalanceService;

import javax.validation.Valid;
import java.security.Principal;
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

    @GetMapping("/stockbalances")
    public ResponseEntity<?> getStockBalances(Principal user){

        User owner = userRepository.findByName(user.getName()).orElseThrow();
        List<FindAllByUserDTO> wallet =  stockBalanceService.findAllByUser(owner.getId());
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @PostMapping("/stockbalances/create")
    public ResponseEntity<UserStockBalances> createStockBalance(@Valid @RequestBody CreateStockBalanceDTO stockBalance, Principal user){

        User owner = userRepository.findByName(user.getName()).orElseThrow();
        UserStockBalances wallet =  stockBalanceService.save(stockBalance.transformaDTO(owner));
        return new ResponseEntity<>(wallet, HttpStatus.CREATED);
    }
}
