package sgcor.tech.public_safety.message.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sgcor.tech.public_safety.exception.UserNotFoundException;
import sgcor.tech.public_safety.message.dto.*;
import sgcor.tech.public_safety.message.entity.Message;
import sgcor.tech.public_safety.message.repository.MessageRepository;
import sgcor.tech.public_safety.users.entity.User;
import sgcor.tech.public_safety.users.repository.UserRepository;
import sgcor.tech.public_safety.users.service.AuthService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public SendResponse sendMessage(SendRequest request){

        User sender = AuthService.getAuthenticatedUser();
        log.info("Authenticated User: " + sender);

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new UserNotFoundException("receiver not found with id " + request.getReceiverId()));

        Message newMessage = Message
                .builder()
                .sender(sender)
                .receiver(receiver)
                .message(request.getMessage())
                .build();

        messageRepository.saveAndFlush(newMessage);

        return SendResponse
                .builder()
                .id(newMessage.getId())
                .message(newMessage.getMessage())
                .senderId(sender.getId())
                .build();
    }

    @Transactional
    public GetAllMessages getMessage(Long userId) {
        User otherUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user not found with id " + userId));

        User authUser = AuthService.getAuthenticatedUser();

        // get sent messages
        List<Message> sentMessages = messageRepository.findAllBySenderIdAndReceiverId(authUser.getId(), otherUser.getId());
        List<Message> receivedMessages = messageRepository.findAllByReceiverIdAndSenderId(authUser.getId(), otherUser.getId());

        List<GetMessage> sent = sentMessages
                .stream().map(message -> {
                    return GetMessage
                            .builder()
                            .id(message.getId())
                            .message(message.getMessage())
                            .timestamp(message.getCreatedAt())
                            .build();
                }).toList();

        List<GetMessage> received = receivedMessages
                .stream().map(message -> {
                    return GetMessage
                            .builder()
                            .id(message.getId())
                            .message(message.getMessage())
                            .timestamp(message.getCreatedAt())
                            .build();
                }).toList();

        return GetAllMessages
                .builder()
                .sent(sent)
                .received(received)
                .build();
    }
}
