package sgcor.tech.public_safety.users.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import sgcor.tech.public_safety.exception.UnauthorizedException;
import sgcor.tech.public_safety.exception.UserAlreadyExist;
import sgcor.tech.public_safety.config.JwtService;
import sgcor.tech.public_safety.users.dto.AuthRequest;
import sgcor.tech.public_safety.users.dto.AuthResponse;
import sgcor.tech.public_safety.users.dto.CreateResponse;
import sgcor.tech.public_safety.users.dto.UserRequest;
import sgcor.tech.public_safety.users.entity.Role;
import sgcor.tech.public_safety.users.entity.User;
import sgcor.tech.public_safety.users.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private UserService underTest;

    private User customUser;

    @BeforeEach
    void setUp() {
        customUser = new User();
        customUser.setId(10L);
        customUser.setEmail("test@user.com");
        customUser.setPassword("encodedPassword");
        customUser.setName("test user");
    }

    @Test
    void shouldCreateUserSuccess() {
        UserRequest request = UserRequest
                .builder()
                .email("user1@mail.com")
                .password("password")
                .build();
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");

        CreateResponse res = underTest.createUser(request);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).saveAndFlush(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser.getEmail()).isEqualTo(request.getEmail());
        assertThat(capturedUser.getPassword()).isEqualTo("encoded-password");
        assertThat(capturedUser.getRole()).isEqualTo(Role.USER);
        assertThat(res.getEmail()).isEqualTo(request.getEmail());
        assertThat(res.getName()).isEqualTo(request.getName());
        assertThat(res.getId()).isEqualTo(capturedUser.getId());
    }

    @Test
    void shouldNotCreateUserIfUserExist() {
        UserRequest request = UserRequest
                .builder()
                .email(customUser.getEmail())
                .password("password")
                .build();

        when(repository.findByEmail(customUser.getEmail())).thenReturn(Optional.of(customUser));

        assertThatThrownBy(() -> underTest.createUser(request))
                .isInstanceOf(UserAlreadyExist.class)
                .hasMessageContaining("user with email already exists");
    }

    @Test
    void shouldAuthenticateUser() {
        AuthRequest request = AuthRequest
                .builder()
                .email(customUser.getEmail())
                .name(customUser.getName())
                .password("password")
                .build();
        when(repository.findByEmail(customUser.getEmail())).thenReturn(Optional.of(customUser));
        when(passwordEncoder.matches("password", customUser.getPassword())).thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn("dummy-token");

        AuthResponse res = underTest.authenticateUser(request);

        assertThat(res.getToken()).isEqualTo("dummy-token");

        verify(repository, times(1)).findByEmail(customUser.getEmail());
        verify(jwtService, times(1)).generateToken(customUser);
    }

    @Test
    void shouldNotAuthenticateIfBadCredential() {
        AuthRequest userRequest = AuthRequest
                .builder()
                .email(customUser.getEmail())
                .password("invalid-password")
                .build();

        when(repository.findByEmail(customUser.getEmail())).thenReturn(Optional.of(customUser));

        assertThatThrownBy(() -> underTest.authenticateUser(userRequest))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("invalid credentials");
    }
}