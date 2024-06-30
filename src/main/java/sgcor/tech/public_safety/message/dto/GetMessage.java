package sgcor.tech.public_safety.message.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class GetMessage {
    private Long id;
    private String message;
    private Date timestamp;
}
