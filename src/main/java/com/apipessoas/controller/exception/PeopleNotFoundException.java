package com.apipessoas.controller.exception;

import java.util.UUID;

public class PeopleNotFoundException extends Exception {
  public PeopleNotFoundException(UUID id) {
    super(String.format("People not found with id %s", id));
  }
}
