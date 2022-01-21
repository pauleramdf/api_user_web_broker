package user.controller;

import com.okta.idx.sdk.api.client.IDXAuthenticationWrapper;
import com.okta.idx.sdk.api.client.ProceedContext;
import com.okta.idx.sdk.api.model.UserProfile;
import com.okta.idx.sdk.api.response.AuthenticationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import user.DTO.CreateUserDTO;
import user.DTO.StockDTO;
import user.DTO.UserResponseDTO;
import user.model.User;
import user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import user.service.StocksService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@RestController
@CrossOrigin
@ComponentScan("com.user.repository")
@RequestMapping(consumes = "application/json")
class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StocksService stocksService;

    @GetMapping(value = "/stocks/{id}", produces = "application/json")
    public ResponseEntity<?> getStocks(@Valid @PathVariable("id") Long id, @RequestHeader("Authorization") String token) {
        StockDTO stock = stocksService.getStock(id, token);
        return new ResponseEntity<>(stock, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}", produces = "application/json")
    public ResponseEntity<?> getUser( @Valid @PathVariable(value = "id") Long user_id, Principal principal) {
        UserResponseDTO u = new UserResponseDTO(userRepository.findById(user_id).orElseThrow());
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @GetMapping(value = "/user", produces = "application/json")
    public ResponseEntity<?>  getUsers() {
        List<UserResponseDTO> ls = new ArrayList<>();
        List<User> list = userRepository.findAll();
        for (User user: list
             ) {
            ls.add(new UserResponseDTO(user));
        }
        return new ResponseEntity<>(ls, HttpStatus.OK);
    }

    @PatchMapping(value = "/user/disable/{id}", produces = "application/json")
    public ResponseEntity<?>  DisableUser(@Valid @PathVariable(value = "id") Long user_id, Principal principal) {

        User user = userRepository.findById(user_id).orElseThrow();
        user.setEnabled(false);
        UserResponseDTO u = new UserResponseDTO(userRepository.save(user));
        return new ResponseEntity<>(u,HttpStatus.OK );
    }

    @PostMapping(value = "/user", produces = "application/json")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserDTO userDTO, Principal principal) {
        User user = userDTO.transformaDTO();
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
    }

    @GetMapping(value = "/hello", produces = "application/json")
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


