package user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.dto.PageDto;
import user.dto.userstockbalances.CreateStockBalanceDTO;
import user.dto.userstockbalances.WalletDTO;
import user.model.UserStockBalances;
import user.service.StockBalanceService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:8080")
@ComponentScan("com.user.repository")
@RequestMapping()
public class StockBalancesRestController {

    private final StockBalanceService stockBalanceService;

    @GetMapping("/stockbalances")
    public ResponseEntity<List<WalletDTO>> getStockBalances(Principal user){
        List<WalletDTO> wallet =  stockBalanceService.findAllByUser(user);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @PostMapping("/stockbalances/page")
    public ResponseEntity<Page<WalletDTO>> getStockBalancesPage(@RequestBody PageDto page, Principal user){
        try {
            Pageable pageable = PageRequest.of(page.getPage(), page.getSize());
            return ResponseEntity.ok().body(stockBalanceService.findAllByUserPage(pageable, user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/stockbalances/create")
    public ResponseEntity<UserStockBalances> createStockBalance(@Valid @RequestBody CreateStockBalanceDTO stockBalance, Principal user){
        UserStockBalances wallet =  stockBalanceService.create(stockBalance, user);
        return new ResponseEntity<>(wallet, HttpStatus.CREATED);
    }
}
