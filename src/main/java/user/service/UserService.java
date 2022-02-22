package user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import user.model.User;
import user.repository.UserRepository;

import java.util.List;
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

    public Optional<User> findByName(String name){
        return userRepository.findByName(name);
    }

    public User save(User user){
        return userRepository.save(user);
    }
    public void getCrendentials() {
//        CredentialsDTO credentialsDTO = new CredentialsDTO();
//        webClient.post().uri("/v1/token").body(credentialsDTO);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}

//criar uma nova table que representa o relacionamento da tabela de ordens com ela mesma
//tem uma chave estrangeira/primaria que é o id da ordem
//
