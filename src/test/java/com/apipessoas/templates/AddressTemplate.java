package com.apipessoas.templates;

import com.apipessoas.controller.dto.AddressRequest;

public class AddressTemplate {

  public static AddressRequest addressDefaultRequest() {
    return AddressRequest.builder()
        .cep("17690")
        .city("Bastos")
        .number("15")
        .street("Rua teste")
        .build();
  }

  public static AddressRequest addressRequestWithoutCepAndCity() {
    return AddressRequest.builder()
        .number("15")
        .street("Rua teste")
        .build();
  }
}
