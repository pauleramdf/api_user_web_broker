package user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.dto.userStockBalances.FindAllByUserDTO;
import user.model.UserStockBalances;
import user.model.UserStockBalancesId;
import user.repository.UserStockBalancesRepository;

import java.util.List;
import java.util.Optional;

@Service("stockBalanceService")
public class StockBalanceService {
    @Autowired
    private UserStockBalancesRepository userStockBalancesRepository;

    public UserStockBalances save(UserStockBalances wallet){
        System.out.println(wallet.getCreated_on());
        System.out.println(wallet.getUpdated_on());
        return userStockBalancesRepository.save(wallet);
    }

    public Optional<UserStockBalances> findById(UserStockBalancesId id){
        return userStockBalancesRepository.findById(id);
    }

    public void addVolumeWallet(UserStockBalances wallet, Long volume){
        wallet.setVolume(wallet.getVolume() + volume);
        userStockBalancesRepository.save(wallet);
    }

    public void subVolumeWallet(UserStockBalances wallet, Long volume){
        wallet.setVolume(wallet.getVolume() - volume);
        userStockBalancesRepository.save(wallet);
    }

    public Optional<UserStockBalances> findWallet(UserStockBalancesId id) {
        return userStockBalancesRepository.findById(id);
    }

    public List<FindAllByUserDTO> findAllByUser(Long id) {
        List<FindAllByUserDTO> wallets = userStockBalancesRepository.findAllByUser(id).stream().map((wallet)-> new FindAllByUserDTO(wallet)).toList();
        return wallets;
    }
}
