package user.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import user.config.ApiUserDefaultException;
import user.dto.AuthenticationRequest;
import user.dto.AuthenticationResponse;
import user.dto.RegisterRequest;
import user.service.AuthenticationService;

@RequestMapping("/auth")
@RequiredArgsConstructor
@Controller
@Slf4j
public class AuthenticationResource {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws ApiUserDefaultException {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
