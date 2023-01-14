package com.apipessoas.controller;

import com.apipessoas.controller.dto.AddressRequest;
import com.apipessoas.controller.dto.AddressResponse;
import com.apipessoas.controller.exception.PeopleNotFoundException;
import com.apipessoas.service.AddressService;
import io.swagger.annotations.ApiOperation;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/people/{peopleId}/address")
public class AddressController {

  private final AddressService service;

  public AddressController(final AddressService service) {
    this.service = service;
  }

  @PostMapping
  @ApiOperation(value = "Realiza a criação de um endereço para uma pessoa")
  private ResponseEntity<?> createAddress(@PathVariable UUID peopleId,
      @Valid @RequestBody final AddressRequest request)
      throws PeopleNotFoundException {

    var address = this.service.create(peopleId, request);
    return ResponseEntity.ok(AddressResponse.builder().id(address.getId()).build());
  }

  @PatchMapping("/{id}")
  @ApiOperation(value = "Atualiza o endereço favorito de uma pessoa pelo id do endereço")
  private ResponseEntity<?> updateFavorite(
      @PathVariable UUID peopleId, @PathVariable UUID id)
      throws PeopleNotFoundException {

    this.service.updateFavorite(peopleId, id);
    return ResponseEntity.ok(null);
  }

}
