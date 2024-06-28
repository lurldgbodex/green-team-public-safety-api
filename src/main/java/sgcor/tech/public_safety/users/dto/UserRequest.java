package sgcor.tech.public_safety.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
    @NotEmpty(message = "name is required")
    private String name;
    @NotEmpty(message = "email is required")
    @Email(message = "provide a valid email")
    private String email;
    @NotEmpty(message = "password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\[\\]{};':\"\\|,.<>\\/\\?]).{8,}$",
            message = "Password must be at least 8 characters long and include at least 1 uppercase letter, " +
                    "1 lowercase letter, 1 digit, and 1 special character.")
    private String password;
}
