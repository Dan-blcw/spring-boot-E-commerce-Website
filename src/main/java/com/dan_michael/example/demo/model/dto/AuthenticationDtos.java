package com.dan_michael.example.demo.model.dto;

import com.dan_michael.example.demo.model.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDtos {

  @JsonProperty("jwt")
  private String jwt;

  @JsonProperty("user")
  private User user;
}
