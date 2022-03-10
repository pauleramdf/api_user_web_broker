package user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import user.dto.user.CreateUserDTO;
import user.dto.stocks.StockDTO;
import user.dto.user.UserResponseDTO;
import user.model.User;
import user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import user.service.StocksService;
import user.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@RestController
@CrossOrigin
@ComponentScan("com.user.repository")
@RequestMapping
class UserRestController {
    @Autowired
    private UserService userService;

//    @GetMapping(value = "/stocks/{id}", produces = "application/json")
//    public ResponseEntity<?> getStocks(@Valid @PathVariable("id") Long id, @RequestHeader("Authorization") String token) {
//        StockDTO stock = stocksService.getStock(id, token);
//        return new ResponseEntity<>(stock, HttpStatus.OK);
//    }

    @GetMapping(value = "/user/{id}", produces = "application/json")
    public ResponseEntity<?> getUser( @Valid @PathVariable(value = "id") Long user_id, Principal principal) {
        UserResponseDTO u = new UserResponseDTO(userService.findById(user_id).orElseThrow());
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @GetMapping(value = "/user", produces = "application/json")
    public ResponseEntity<?>  getUsers() {
        try{
            List<UserResponseDTO> users = userService.findAll();

            return new ResponseEntity<>(users, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

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
    public String sayHello( Principal user, @AuthenticationPrincipal Jwt jwt) {
         return String.format("Hello, %s!", user.getName());
    }
}


