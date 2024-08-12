package com.dan_michael.example.demo.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Material {
    @Id
    @GeneratedValue
    private Integer id;
    private String image_url;
    private String name;
    @Column(length = 10485760)
    private String description;
    private Integer status;
    @CreatedDate
    @Column(
            nullable = false,
            updatable = false
    )
    private Date createDate;

}