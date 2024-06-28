package sgcor.tech.public_safety.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import sgcor.tech.public_safety.exception.ValidationError;
import sgcor.tech.public_safety.shared.CustomError;
import sgcor.tech.public_safety.users.dto.AuthRequest;
import sgcor.tech.public_safety.users.dto.AuthResponse;
import sgcor.tech.public_safety.users.dto.CreateResponse;
import sgcor.tech.public_safety.users.dto.UserRequest;
import sgcor.tech.public_safety.users.entity.User;
import sgcor.tech.public_safety.users.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private User customUser;

    @BeforeEach
    void setUp() {
        customUser = User
                .builder()
                .name("TestUser28")
                .email("test@user20.com")
                .password("S3CuReDP@$$woRd")
                .build();

        userRepository.save(customUser);
    }

    @Test
    void CreateNewUserSuccessTest() {
        UserRequest request = UserRequest
                .builder()
                .name("test user10")
                .email("test@user10.com")
                .password("S3CuR3dP@$$woRd")
                .build();

        ResponseEntity<CreateResponse> res = restTemplate.postForEntity("/api/users/register", request, CreateResponse.class);
        CreateResponse responseBody = res.getBody();

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getName()).isEqualTo(request.getName());
        assertThat(responseBody.getEmail()).isEqualTo(request.getEmail());
    }

    @Test
    void createNewUserNoRequestBodyTest() {
        UserRequest request = UserRequest.builder().build();

        ResponseEntity<ValidationError> res = restTemplate.postForEntity("/api/users/register", request, ValidationError.class);

        ValidationError errorBody = res.getBody();

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorBody).isNotNull();
        assertThat(errorBody.errors()).hasFieldOrPropertyWithValue("name", "name is required");
        assertThat(errorBody.errors()).hasFieldOrPropertyWithValue("email", "email is required");
        assertThat(errorBody.errors()).hasFieldOrPropertyWithValue("password", "password is required");
    }

    @Test
    void createNewUserWithInvalidEmailAndPassword() {
        UserRequest request = UserRequest
                .builder()
                .name("test user")
                .email("invalid-email-address")
                .password("password")
                .build();

        ResponseEntity<ValidationError> res = restTemplate.postForEntity("/api/users/register", request, ValidationError.class);
        ValidationError errorBody = res.getBody();

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorBody).isNotNull();
        assertThat(errorBody.errors()).hasFieldOrPropertyWithValue("email", "provide a valid email");
        assertThat(errorBody.errors()).hasFieldOrPropertyWithValue("password", "Password must be at least 8 characters long and include at least 1 uppercase letter, " +
                "1 lowercase letter, 1 digit, and 1 special character.");
    }

    @Test
    void createUserWhenUserAlreadyExist() {
        UserRequest request = UserRequest
                .builder()
                .name(customUser.getName())
                .email(customUser.getEmail())
                .password("S3CuR3dP@$$woRd")
                .build();

        ResponseEntity<CustomError> response = restTemplate.postForEntity("/api/users/register", request, CustomError.class);
        CustomError error = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertThat(error).isNotNull();
        assertThat(error.error()).isEqualTo("user with email already exists");
    }

//    @Test
//    void authenticationUserSuccessTest() {
//        AuthRequest request = AuthRequest
//                .builder()
//                .name(customUser.getName())
//                .email(customUser.getEmail())
//                .password("S3CuReDP@$$woRd")
//                .build();

        // authenticate the user
//        AuthResponse authResponse = restTemplate.postForObject("/api/users/authenticate", request, AuthResponse.class);

//        assertThat(authResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(authResponse.getToken()).isNotNull();
//        assertThat(authResponse.getBody().getToken()).isNotNull();
//    }

//    @Test
//    void authenticateWithInvalidCredentialTest() {
//        AuthRequest request = AuthRequest
//                .builder()
//                .name(customUser.getName())
//                .email(customUser.getEmail())
//                .password("invalid-password")
//                .build();
//
//        ResponseEntity<CustomError> authError = restTemplate.postForEntity("/api/users/authenticate", request, CustomError.class);
//
//        assertThat(authError.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
//        assertThat(authError.getBody()).isNotNull();
//        assertThat(authError.getBody().error()).isEqualTo("invalid credentials");
//    }
}
