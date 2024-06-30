package sgcor.tech.public_safety.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendRequest {
    @NotBlank
    private String message;
    @NotNull
    private Long receiverId;
}
