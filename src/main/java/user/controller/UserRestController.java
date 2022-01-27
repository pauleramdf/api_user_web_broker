package user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import user.dto.CreateUserDTO;
import user.dto.StockDTO;
import user.dto.UserResponseDTO;
import user.model.User;
import user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import user.service.StocksService;
import user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@RestController
@CrossOrigin
@ComponentScan("com.user.repository")
@RequestMapping
class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StocksService stocksService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/stocks/{id}", produces = "application/json")
    public ResponseEntity<?> getStocks(@Valid @PathVariable("id") Long id, @RequestHeader("Authorization") String token) {
        StockDTO stock = stocksService.getStock(id, token);
        return new ResponseEntity<>(stock, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}", produces = "application/json")
    public ResponseEntity<?> getUser( @Valid @PathVariable(value = "id") Long user_id, Principal principal) {
        UserResponseDTO u = new UserResponseDTO(userService.findById(user_id).orElseThrow());
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @GetMapping(value = "/user", produces = "application/json")
    public ResponseEntity<?>  getUsers() {
        List<UserResponseDTO> ls = new ArrayList<>();
        List<User> list = userService.findAll();
        for (User user: list
             ) {
            ls.add(new UserResponseDTO(user));
        }
        return new ResponseEntity<>(ls, HttpStatus.OK);
    }

    @PatchMapping(value = "/user/disable/{id}", produces = "application/json")
    public ResponseEntity<?>  DisableUser(@Valid @PathVariable(value = "id") Long user_id, Principal principal) {

        User user = userService.findById(user_id).orElseThrow();
        user.setEnabled(false);
        UserResponseDTO u = new UserResponseDTO(userService.save(user));
        return new ResponseEntity<>(u,HttpStatus.OK );
    }

    @PostMapping(value = "/user", produces = "application/json")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserDTO userDTO, Principal principal) {
        User user = userDTO.transformaDTO();
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @GetMapping(value = "/hello", produces = "application/json")
    public String sayHello( java.security.Principal user, @AuthenticationPrincipal Jwt jwt) {
         return String.format("Hello, %s!", user);
    }


}


