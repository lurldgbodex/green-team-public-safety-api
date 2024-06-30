package sgcor.tech.public_safety.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sgcor.tech.public_safety.message.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m JOIN FETCH m.sender s JOIN FETCH m.receiver r WHERE m.sender.id = :senderId AND m.receiver.id = :receiverId")
    List<Message> findAllBySenderIdAndReceiverId(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
    @Query("SELECT m FROM Message m JOIN FETCH m.sender s JOIN FETCH m.receiver r WHERE m.sender.id = :receiverId AND m.receiver.id = :senderId")
    List<Message> findAllByReceiverIdAndSenderId(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
}
