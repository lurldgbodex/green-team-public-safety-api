package sgcor.tech.public_safety.exception;

import java.util.Map;

public record ValidationError(Map<String, String> errors) {
}
