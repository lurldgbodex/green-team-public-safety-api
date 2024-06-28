package sgcor.tech.public_safety.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sgcor.tech.public_safety.users.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
