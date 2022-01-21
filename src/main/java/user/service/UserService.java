package user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import user.model.User;
import user.repository.UserRepository;

import java.util.Optional;

@Service("userService")
public class UserService {
    @Autowired
    private WebClient webClient;
    @Autowired
    private UserRepository userRepository;

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }
    public void addDollarBalance(User user, double value){
        user.setDollar_balance(user.getDollar_balance() + value);
        userRepository.save(user);
    }

    public void subDollarBalance(User user, double value){
        user.setDollar_balance(user.getDollar_balance() - value);
        userRepository.save(user);
    }
}
