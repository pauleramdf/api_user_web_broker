package user.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication")
public class AuthenticationResource {

    private final AuthenticationService service;

    @Operation(summary = "register new User",
            description = ".Ex.: '/auth/register ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully created")
            })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws ApiUserDefaultException {
        return ResponseEntity.ok(service.register(request));
    }

    @Operation(summary = "authenticate User",
            description = ".Ex.: '/auth/authenticate ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully authenticated")
            })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
