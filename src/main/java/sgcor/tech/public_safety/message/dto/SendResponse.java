package sgcor.tech.public_safety.message.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendResponse {
    private Long id;
    private String message;
    private Long senderId;
}
