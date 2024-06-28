package sgcor.tech.public_safety.users.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sgcor.tech.public_safety.users.entity.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase
class UserRepositoryTest {
    @Autowired
    private UserRepository underTest;

    @Test
    @Transactional
    void shouldFindByEmail() {
        User newUser = User
                .builder()
                .name("test user")
                .email("test@user.com")
                .password("encoded-password")
                .build();
        underTest.saveAndFlush(newUser);

        Optional<User> response = underTest.findByEmail("test@user.com");
        assertThat(response).isPresent();
        assertThat(response).contains(newUser);
    }

    @Test
    void shouldNotFindByEmailIfEmailNotExist() {
        Optional<User> response = underTest.findByEmail("invalid@user.com");
        assertThat(response).isEmpty();
    }
}