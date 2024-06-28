package sgcor.tech.public_safety.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sgcor.tech.public_safety.exception.UnauthorizedException;
import sgcor.tech.public_safety.exception.UserAlreadyExist;
import sgcor.tech.public_safety.exception.UserNotFoundException;
import sgcor.tech.public_safety.security.JwtService;
import sgcor.tech.public_safety.users.dto.AuthRequest;
import sgcor.tech.public_safety.users.dto.AuthResponse;
import sgcor.tech.public_safety.users.dto.CreateResponse;
import sgcor.tech.public_safety.users.dto.UserRequest;
import sgcor.tech.public_safety.users.entity.Role;
import sgcor.tech.public_safety.users.entity.User;
import sgcor.tech.public_safety.users.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user not found with email"));
    }
    public CreateResponse createUser(UserRequest req) {
       boolean userExists = userRepository.findByEmail(req.getEmail()).isPresent();

       if (userExists) throw new UserAlreadyExist("user with email already exists");

       String encodedPassword = passwordEncoder.encode(req.getPassword());

       User newUser = User
               .builder()
               .name(req.getName())
               .email(req.getEmail())
               .role(Role.USER)
               .password(encodedPassword)
               .build();

       userRepository.saveAndFlush(newUser);

       return CreateResponse
               .builder()
               .id(newUser.getId())
               .name(newUser.getName())
               .email(newUser.getEmail())
               .build();
    }

    public AuthResponse authenticateUser(AuthRequest req) {
        try {
            User user = findByEmail(req.getEmail());

            boolean validPassword = passwordEncoder.matches(req.getPassword(), user.getPassword());
            boolean validCredential = user.getName().equals(req.getName());
            if (validPassword && validCredential) {
                String token = jwtService.generateToken(user);
                return AuthResponse
                        .builder()
                        .token(token)
                        .build();
            } else throw new UnauthorizedException("invalid credentials");
        } catch(UserNotFoundException ue) {
            throw new UnauthorizedException("invalid credentials");
        }
    }


}
