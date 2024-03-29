package user.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.dto.userstockbalances.CreateStockBalanceDTO;
import user.dto.userstockbalances.WalletDTO;
import user.model.UserStockBalances;
import user.service.StockBalanceService;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stockbalances")
public class StockBalancesRestController {

    private final StockBalanceService stockBalanceService;

    @GetMapping()
    public ResponseEntity<List<WalletDTO>> getStockBalances(Principal user) {
        List<WalletDTO> wallet = stockBalanceService.findAllByUser(user);
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @PostMapping("/page")
    public ResponseEntity<Page<WalletDTO>> getStockBalancesPage(Pageable pageable, Principal user) {
        try {
            return ResponseEntity.ok().body(stockBalanceService.findAllByUserPage(pageable, user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<UserStockBalances> createStockBalance(@Valid @RequestBody CreateStockBalanceDTO stockBalance, Principal user) {
        UserStockBalances wallet = stockBalanceService.create(stockBalance, user);
        return new ResponseEntity<>(wallet, HttpStatus.CREATED);
    }
}
