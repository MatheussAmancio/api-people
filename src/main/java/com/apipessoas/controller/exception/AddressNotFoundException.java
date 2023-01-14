package com.apipessoas.controller.exception;

import java.util.UUID;

public class AddressNotFoundException extends Exception {
  public AddressNotFoundException(UUID id) {
    super(String.format("Address not found with id %s", id));
  }
}
