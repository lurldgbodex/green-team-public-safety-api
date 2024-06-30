package sgcor.tech.public_safety.message.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sgcor.tech.public_safety.message.dto.GetAllMessages;
import sgcor.tech.public_safety.message.dto.SendRequest;
import sgcor.tech.public_safety.message.dto.SendResponse;
import sgcor.tech.public_safety.message.service.MessageService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/new")
    public ResponseEntity<SendResponse> sendMessage(@RequestBody @Valid SendRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(messageService.sendMessage(request));
    }


    @GetMapping("/{userId}")
    public ResponseEntity<GetAllMessages> getMessages(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(messageService.getMessage(userId));
    }
}
