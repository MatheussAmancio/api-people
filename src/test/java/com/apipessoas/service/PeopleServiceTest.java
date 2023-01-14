package com.apipessoas.service;

import static com.apipessoas.templates.PeopleTemplate.peopleDefaultRequest;
import static com.apipessoas.templates.PeopleTemplate.peopleRequestWithIncorrectDateOfBirth;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.apipessoas.BaseTest;
import com.apipessoas.controller.exception.PeopleNotFoundException;
import com.apipessoas.repository.PeopleRepository;
import com.apipessoas.utils.DateUtils;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PeopleServiceTest extends BaseTest {

  static final UUID PEOPLE_ID = UUID.fromString("c7078f22-2aad-4578-928c-974db4cd7b89");

  @Autowired
  private PeopleService service;

  @SpyBean
  PeopleRepository repository;

  @Test
  @DisplayName("deve criar uma pessoa.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldCreateAPeople() {

    // arrange
    var request = peopleDefaultRequest();

    // act
    var people = service.create(request);

    // assert
    verify(repository, times(1)).save(any());
    assertEquals(request.getName(), people.getName());
    assertEquals(DateUtils.dateFromString(request.getDateOfBirth()), people.getDateOfBirth());
    assertNotNull(people.getId());
  }

  @Test
  @DisplayName("Deve buscar uma pessoa.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql", "/mocks/people/insert.sql"})
  void shouldReturnAPeople() throws PeopleNotFoundException {

    // act
    var people = service.find(PEOPLE_ID);

    // assert
    verify(this.repository, times(1)).findById(any());
    assertEquals(PEOPLE_ID, people.getId());
  }

  @Test
  @DisplayName("Deve buscar uma pessoa com endereço.")
  @Sql({
      "/mocks/address/clear.sql",
      "/mocks/people/clear.sql",
      "/mocks/people/insert.sql",
      "/mocks/address/insert.sql"
  })
  void shouldReturnAPeopleWithAddress() throws PeopleNotFoundException {

    // act
    var people = service.find(PEOPLE_ID);

    // assert
    verify(this.repository, times(1)).findById(any());
    assertEquals(PEOPLE_ID, people.getId());
    assertNotNull(people.getAddress());
  }

  @Test
  @DisplayName("Não deve retornar uma pessoa quando pessoa não encontrada.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldNotReturnAPeopleWhenPeopleNotFound() {

    // assert
    assertThrows(PeopleNotFoundException.class, () -> {
      service.find(PEOPLE_ID);
    });
    verify(this.repository, times(1)).findById(any());
  }

  @Test
  @DisplayName("Deve listar todas as pessoas de forma paginada.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/insert.sql", "/mocks/people/insert_02.sql"})
  void shouldListAllPeoplesWithPagination() {

    // arrange
    Pageable paging = PageRequest.of(0, 2, Sort.by("name"));

    // act
    var peoples = service.list(paging);

    // assert
    verify(this.repository, times(1)).findAll((Pageable) any());
    assertEquals(2, peoples.getTotalElements());
    assertEquals(UUID.fromString("c7078f22-2aad-4578-928c-974db4cd7b89"),
        peoples.getContent().get(0).getId());
    assertEquals(UUID.fromString("b194e79a-3aee-4984-ae92-dcf35b52ec73"),
        peoples.getContent().get(1).getId());
  }

  @Test
  @DisplayName("Deve atualizar uma pessoa.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/insert.sql"})
  void shouldUpdateAPeople() throws PeopleNotFoundException {

    // arrange
    var request = peopleDefaultRequest();

    // act
    var people = service.update(PEOPLE_ID, request);

    // assert
    verify(this.repository, times(1)).save(any());
    assertEquals(PEOPLE_ID, people.getId());
    assertEquals(request.getName(), people.getName());
    assertEquals(DateUtils.dateFromString(request.getDateOfBirth()), people.getDateOfBirth());
  }

  @Test
  @DisplayName("Não deve atualizar uma pessoa quando pessoa não encontrada.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldNotUpdateAPeopleWhenPeopleNotFound() {

    // arrange
    var request = peopleRequestWithIncorrectDateOfBirth();

    // assert
    assertThrows(PeopleNotFoundException.class, () -> {
      service.update(PEOPLE_ID, request);
    });
    verify(this.repository, times(0)).save(any());
  }
}
