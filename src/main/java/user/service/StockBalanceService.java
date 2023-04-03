package user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import user.dto.userstockbalances.CreateStockBalanceDTO;
import user.dto.userstockbalances.WalletDTO;
import user.model.User;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;
import user.repository.UserStockBalancesRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service("stockBalanceService")
@RequiredArgsConstructor
public class StockBalanceService {

    private final UserStockBalancesRepository userStockBalancesRepository;

    private final UserService userService;

    public UserStockBalances create(CreateStockBalanceDTO stockBalance, Principal user) {
        User owner = userService.findByName(user.getName()).orElseThrow();
        UserStockBalancesId id = new UserStockBalancesId(owner, stockBalance.getIdStock());
        var wallet = userStockBalancesRepository.findById(id);
        if(wallet.isPresent()){
            wallet.get().setVolume(wallet.get().getVolume() + stockBalance.getVolume());
            return userStockBalancesRepository.save(wallet.get());
        }else{
            return userStockBalancesRepository.save(stockBalance.transformaDTO(owner));
        }
    }

    public UserStockBalances save(UserStockBalances stockBalance) {
        return userStockBalancesRepository.save(stockBalance);
    }

    public Optional<UserStockBalances> findById(UserStockBalancesId id) {
        return userStockBalancesRepository.findById(id);
    }

    public void addVolumeWallet(UserStockBalances wallet, Long volume) {
        wallet.setVolume(wallet.getVolume() + volume);
        userStockBalancesRepository.save(wallet);
    }

    public void subVolumeWallet(UserStockBalances wallet, Long volume) {
        wallet.setVolume(wallet.getVolume() - volume);
        userStockBalancesRepository.save(wallet);
    }

    public Optional<UserStockBalances> findWallet(UserStockBalancesId id) {
        return userStockBalancesRepository.findById(id);
    }

    public List<WalletDTO> findAllByUser(Principal user) {
        User owner = userService.findByName(user.getName()).orElseThrow();
        return userStockBalancesRepository.findAllByUser(owner.getId()).stream().map(WalletDTO::new).toList();
    }

    public Page<WalletDTO> findAllByUserPage(Pageable pageable, Principal user) {
        User owner = userService.findByName(user.getName()).orElseThrow();
        Page<UserStockBalances> walletsPage = userStockBalancesRepository.findAllByUserPaged(pageable, owner.getId());
        return walletsPage.map(WalletDTO::new);
    }

}
