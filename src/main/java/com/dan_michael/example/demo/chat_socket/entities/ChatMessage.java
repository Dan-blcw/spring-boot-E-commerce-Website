package com.dan_michael.example.demo.chat_socket.entities;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {
    @Id
    @GeneratedValue
    private Integer id;
    private String chatId;
    private String action;
    private String senderId;
    private String senderImage;
    private String recipientId;
    @Column(length = 10485760)
    private String content;
    private Date timestamp;
}


