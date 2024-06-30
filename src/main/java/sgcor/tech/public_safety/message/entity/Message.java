package sgcor.tech.public_safety.message.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import sgcor.tech.public_safety.users.entity.User;

import java.util.Date;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message {
    @Getter @Setter
    @Id @GeneratedValue
    private Long id;

    @Getter @Setter
    @Column(nullable = false, length = 1000)
    private String message;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Getter @Setter
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
