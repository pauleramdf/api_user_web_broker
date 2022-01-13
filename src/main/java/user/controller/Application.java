package user.controller;

import com.okta.idx.sdk.api.client.IDXAuthenticationWrapper;
import com.okta.idx.sdk.api.client.ProceedContext;
import com.okta.idx.sdk.api.model.UserProfile;
import com.okta.idx.sdk.api.response.AuthenticationResponse;
import org.springframework.web.bind.annotation.*;
import user.model.User;
import user.model.UserOrders;
import user.repository.UserOrdersRepository;
import user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import user.repository.UserStockBalancesRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
@CrossOrigin
@ComponentScan("com.user.repository")
class UserRestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserOrdersRepository orderRepository;
    @Autowired
    private UserStockBalancesRepository stockBalanceRepository;

    String getCaffeineLevel() {
        List<String> givenList = Arrays.asList(
                "Head on table asleep. Needs coffee now!",
                "Not at all. What's wrong?!",
                "Mildly. Boring.",
                "Making progress.",
                "Everything is awesome. Stuff is definitely happening.",
                "Eyeballs are rolling around in my head and I'm shouting at my coworker about JHipster.",
                "The LD50 of caffeine is 100 cups. Your developer has had 99 and is talking to the bike rack outside while jogging in place."
        );
        Random rand = new Random();
        String caffeineLevelString = givenList.get(rand.nextInt(givenList.size()));
        return caffeineLevelString;
    }

    @GetMapping("/howcaffeinatedami")
    public String getCaffeineLevel(Principal principal) {
        String userName = principal != null ? principal.getName() : "Anonymous";
        return userName + ", your developer's caffeine level is: " + getCaffeineLevel();
    }

    @GetMapping("/users")
    public List <User> getUsers(Principal principal) {
        return userRepository.findAll();
        //return principal.toString();
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user, Principal principal) {
        return userRepository.save(user);
    }

    @PostMapping("/users/order")
    public UserOrders createOrder(@Valid @RequestBody UserOrders order, Principal principal) {
        if(order.getType() == 1){
            //implementar logica para a venda;
        }
        else{
            //implementar logica para a compra;
        }
        return orderRepository.save(order);
    }

    private boolean createUserOkta(){
        try{
            IDXAuthenticationWrapper idxAuthenticationWrapper = new IDXAuthenticationWrapper();
            AuthenticationResponse beginResponse = idxAuthenticationWrapper.begin();

            ProceedContext beginProceedContext = beginResponse.getProceedContext();

            AuthenticationResponse newUserRegistrationResponse = idxAuthenticationWrapper.fetchSignUpFormValues(beginProceedContext);

            UserProfile userProfile = new UserProfile();
            userProfile.addAttribute("lastName", "lastname");
            userProfile.addAttribute("firstName", "firstname");
            userProfile.addAttribute("email", "email");
            userProfile.addAttribute("password", "password");


            ProceedContext proceedContext = newUserRegistrationResponse.getProceedContext();

            AuthenticationResponse authenticationResponse =
                    idxAuthenticationWrapper.register(proceedContext, userProfile);
        }catch(Exception e){
            return false;
        }
        return true;
    }

}


