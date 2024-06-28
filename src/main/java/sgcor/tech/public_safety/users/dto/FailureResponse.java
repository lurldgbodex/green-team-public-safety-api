package sgcor.tech.public_safety.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FailureResponse {
    private String message;
}
