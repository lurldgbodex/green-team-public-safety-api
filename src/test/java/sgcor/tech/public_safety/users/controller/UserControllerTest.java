package sgcor.tech.public_safety.users.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import sgcor.tech.public_safety.config.JwtService;
import sgcor.tech.public_safety.users.dto.AuthRequest;
import sgcor.tech.public_safety.users.dto.AuthResponse;
import sgcor.tech.public_safety.users.dto.CreateResponse;
import sgcor.tech.public_safety.users.dto.UserRequest;
import sgcor.tech.public_safety.users.entity.User;
import sgcor.tech.public_safety.users.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtService jwtService;
    private User testUser;
    private UserRequest request;

    @BeforeEach
    void setUp() {
        testUser = User
                .builder()
                .id(10L)
                .name("Test User")
                .email("test@user.com")
                .password("s3cUr3d-P@ssw0Rd")
                .build();

        request = UserRequest
                .builder()
                .name(testUser.getName())
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .build();
    }

    @Test
    void shouldCreateMockMvc() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void shouldCreateNewUserTest() throws Exception {
        CreateResponse res = CreateResponse
                .builder()
                .name(testUser.getName())
                .email(testUser.getEmail())
                .id(testUser.getId())
                .build();
        when(userService.createUser(request)).thenReturn(res);

        ObjectMapper objectMapper = new ObjectMapper();
        String createRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(res.getName()))
                .andExpect(jsonPath("$.email").value(res.getEmail()))
                .andExpect(jsonPath("$.id").value(res.getId()));
    }

    @Test
    void shouldAuthenticateTest() throws Exception {
        String token = "auth-token";
        AuthResponse response  = AuthResponse
                .builder()
                .token(token)
                .build();

        AuthRequest authRequest = AuthRequest
                .builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        when(userService.authenticateUser(authRequest)).thenReturn(response);

        ObjectMapper mapper = new ObjectMapper();
        String authRequestString = mapper.writeValueAsString(request);
        mockMvc.perform(post("/api/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authRequestString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value(token));
    }
}