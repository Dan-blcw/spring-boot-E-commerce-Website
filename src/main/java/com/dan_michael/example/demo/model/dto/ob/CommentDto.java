package com.dan_michael.example.demo.model.dto.ob;

import com.dan_michael.example.demo.model.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private String content;
    private User comment_user;
}
