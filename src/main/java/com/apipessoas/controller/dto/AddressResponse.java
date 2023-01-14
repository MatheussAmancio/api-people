package com.apipessoas.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AddressResponse {

  private UUID id;

  @JsonInclude(Include.NON_NULL)
  private String street;

  @JsonInclude(Include.NON_NULL)
  private String cep;

  @JsonInclude(Include.NON_NULL)
  private String number;

  @JsonInclude(Include.NON_NULL)
  private String city;
}
