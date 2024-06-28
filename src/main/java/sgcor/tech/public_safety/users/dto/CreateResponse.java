package sgcor.tech.public_safety.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateResponse {
    private Long id;
    private String name;
    private String email;
}
