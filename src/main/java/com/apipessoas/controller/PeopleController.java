package com.apipessoas.controller;

import com.apipessoas.controller.dto.PeopleRequest;
import com.apipessoas.controller.dto.PeopleResponse;
import com.apipessoas.controller.exception.PeopleNotFoundException;
import com.apipessoas.service.PeopleService;
import io.swagger.annotations.ApiOperation;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/people")
public class PeopleController {

  private final PeopleService service;

  public PeopleController(final PeopleService service) {
    this.service = service;
  }

  @PostMapping
  @ApiOperation(value = "Realiza a criação de uma pessoa")
  private ResponseEntity<?> createPeople(@Valid @RequestBody final PeopleRequest request) {

    var people = this.service.create(request);
    return ResponseEntity.ok(PeopleResponse.builder().id(people.getId()).build());
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Realiza a atualização de uma pessoa pelo id")
  private ResponseEntity<?> updatePeople(
      @PathVariable UUID id, @Valid @RequestBody final PeopleRequest request)
      throws PeopleNotFoundException {

    var people = this.service.update(id, request);
    return ResponseEntity.ok(PeopleResponse.builder().id(people.getId()).build());
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Realiza a busca de uma pessoa pelo id")
  private ResponseEntity<?> findPeople(@PathVariable UUID id)
      throws PeopleNotFoundException {

    this.service.find(id);
    var people = this.service.find(id);
    return ResponseEntity.ok(
        PeopleResponse.builder()
            .id(people.getId())
            .name(people.getName())
            .dateOfBirth(people.getDateOfBirth())
            .address(people.getAddress())
            .build()
    );
  }

  @GetMapping
  @ApiOperation(value = "Realiza a busca de todas as pessoas de forma paginada")
  private ResponseEntity<?> listPeople(Pageable pageable) {

    this.service.list(pageable);
    var people = this.service.list(pageable);
    return ResponseEntity.ok(people);
  }
}
