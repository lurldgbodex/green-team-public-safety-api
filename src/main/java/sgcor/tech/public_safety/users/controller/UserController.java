package sgcor.tech.public_safety.users.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sgcor.tech.public_safety.users.dto.AuthRequest;
import sgcor.tech.public_safety.users.dto.AuthResponse;
import sgcor.tech.public_safety.users.dto.CreateResponse;
import sgcor.tech.public_safety.users.dto.UserRequest;
import sgcor.tech.public_safety.users.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<CreateResponse> createNewUser(@RequestBody @Valid UserRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(req));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody @Valid AuthRequest req) {
        return ResponseEntity.ok(userService.authenticateUser(req));
    }
}
