package user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import user.dto.user.CreateUserDTO;
import user.dto.user.UserResponseDTO;
import user.model.User;
import org.springframework.context.annotation.ComponentScan;
import user.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@ComponentScan("com.user.repository")
@RequestMapping
@RequiredArgsConstructor
class UserRestController {
    private final UserService userService;

    @GetMapping(value = "/user/info", produces = "application/json")
    public ResponseEntity<UserResponseDTO> getUser(Principal principal) {
        String username = principal.getName();
        UserResponseDTO u = new UserResponseDTO(userService.findByName(username).orElseThrow());
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @GetMapping(value = "/user", produces = "application/json")
    public ResponseEntity<List<UserResponseDTO>>  getUsers() {
        try{
            List<UserResponseDTO> users = userService.findAll();

            return new ResponseEntity<>(users, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

    }

    @PatchMapping(value = "/user/disable/{id}", produces = "application/json")
    public ResponseEntity<UserResponseDTO> disableUser(@Valid @PathVariable(value = "id") Long userId, Principal principal) {

        User user = userService.findById(userId).orElseThrow();
        user.setEnabled(false);
        UserResponseDTO u = new UserResponseDTO(userService.save(user));
        return new ResponseEntity<>(u,HttpStatus.OK );
    }

    @PostMapping(value = "/user", produces = "application/json")
    public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserDTO userDTO, Principal principal) {
        User user = userDTO.transformaDTO();
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @GetMapping(value = "/hello", produces = "application/json")
    public String sayHello( Principal user, @AuthenticationPrincipal Jwt jwt) {
         return String.format("Hello, %s!", user.getName());
    }
}


