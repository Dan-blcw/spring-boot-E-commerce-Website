package com.dan_michael.example.demo.chat_socket.entities;

import com.dan_michael.example.demo.model.entities.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserAccountInfo {
    @Id
    private String name;
    private String fullName;
    private String img_url;

    @Enumerated(EnumType.STRING)
    private Role role;
    private Status status;
}
