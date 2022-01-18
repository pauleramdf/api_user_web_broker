package user.controller;

import com.okta.idx.sdk.api.client.IDXAuthenticationWrapper;
import com.okta.idx.sdk.api.client.ProceedContext;
import com.okta.idx.sdk.api.model.UserProfile;
import com.okta.idx.sdk.api.response.AuthenticationResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import user.DTO.StockDTO;
import user.model.User;
import user.model.UserOrders;
import user.repository.UserOrdersRepository;
import user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import user.repository.UserStockBalancesRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@CrossOrigin
@ComponentScan("com.user.repository")
class UserRestController {

    @Autowired
    private WebClient webClient;
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

    @GetMapping(value = "/user/{id}", produces = "application/json")
    public Optional<User> getUsers(@PathVariable(value = "id") Long user_id, Principal principal) {
        return userRepository.findById(user_id);
    }

    @PatchMapping(value = "/user/disable/{id}", produces = "application/json")
    public User DisableUser(@PathVariable(value = "id") Long user_id, Principal principal) {
        User user = userRepository.findById(user_id).orElseThrow(Error :: new);

        user.setEnabled(false);
        user.setUpdated_on(Timestamp.valueOf(LocalDateTime.now()));

        return userRepository.save(user);
    }

    @PostMapping("/user")
    public User createUser(@Valid @RequestBody User user, Principal principal) {
        return userRepository.save(user);
    }



    @GetMapping("/hello")
    public OidcUserInfo sayHello(@AuthenticationPrincipal OidcUser oidcUser) {
        return oidcUser.getUserInfo();
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


