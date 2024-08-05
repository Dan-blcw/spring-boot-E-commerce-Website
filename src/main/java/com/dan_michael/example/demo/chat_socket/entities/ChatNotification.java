package com.dan_michael.example.demo.chat_socket.entities;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
    private Integer id;
    private String senderId;
    private String senderImage;
    private String recipientId;
    @Column(length = 10485760)
    private String content;
}
