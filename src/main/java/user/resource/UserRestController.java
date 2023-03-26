package user.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.dto.user.UserResponseDTO;
import user.model.User;
import user.service.UserService;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
class UserRestController {
    private final UserService userService;

    @GetMapping(value = "/info", produces = "application/json")
    public ResponseEntity<UserResponseDTO> getUser(Principal principal) {
        String username = principal.getName();
        UserResponseDTO u = new UserResponseDTO(userService.findByName(username).orElseThrow());
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @GetMapping( produces = "application/json")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        try {
            List<UserResponseDTO> users = userService.findAll();

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @PatchMapping(value = "/disable/{id}", produces = "application/json")
    public ResponseEntity<UserResponseDTO> disableUser(@Valid @PathVariable(value = "id") Long userId, Principal principal) {

        User user = userService.findById(userId).orElseThrow();
        user.setEnabled(false);
        UserResponseDTO u = new UserResponseDTO(userService.save(user));
        return new ResponseEntity<>(u, HttpStatus.OK);
    }
}


