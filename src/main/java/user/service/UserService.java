package user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import user.dto.user.UserResponseDTO;
import user.model.User;
import user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("userService")
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

        public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }
    public void addDollarBalance(User user, double value){
        user.setDollarBalance(user.getDollarBalance() + value);
        userRepository.save(user);
    }

    public void subDollarBalance(User user, double value){
        user.setDollarBalance(user.getDollarBalance() - value);
        userRepository.save(user);
    }

    public Optional<User> findByName(String name){
        return userRepository.findByName(name);
    }

    public User save(User user){
        return userRepository.save(user);
    }


    public List<UserResponseDTO> findAll() {
        List<UserResponseDTO> ls = new ArrayList<>();
        List<User> list = userRepository.findAll();
        for (User user: list
        ) {
            ls.add(new UserResponseDTO(user));
        }
        return ls;
    }
}
