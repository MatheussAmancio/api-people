package com.apipessoas.controller.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressRequest {

  @NotBlank(message = "street is required.")
  private String street;

  @NotBlank(message = "cep is required.")
  private String cep;

  @NotBlank(message = "number is required.")
  private String number;

  @NotBlank(message = "city is required.")
  private String city;
}
