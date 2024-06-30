package sgcor.tech.public_safety.message.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetAllMessages {
    private List<GetMessage> sent;
    private List<GetMessage> received;
}
