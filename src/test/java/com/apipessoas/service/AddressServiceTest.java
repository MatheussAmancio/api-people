package com.apipessoas.service;

import static com.apipessoas.templates.AddressTemplate.addressDefaultRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.apipessoas.BaseTest;
import com.apipessoas.controller.exception.PeopleNotFoundException;
import com.apipessoas.repository.AddressRepository;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AddressServiceTest extends BaseTest {

  static final UUID PEOPLE_ID = UUID.fromString("c7078f22-2aad-4578-928c-974db4cd7b89");
  static final UUID ADDRESS_ID = UUID.fromString("e23edd2c-0cd0-4461-a6b4-1650df33666a");

  @Autowired
  private AddressService service;

  @SpyBean
  AddressRepository repository;

  @SpyBean
  PeopleService peopleService;

  @Test
  @DisplayName("deve criar um endereço para uma pessoa existente como favorito.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql", "/mocks/people/insert.sql"})
  void shouldCreateAnAddressWhenExistsPeopleWithoutAddress() throws PeopleNotFoundException {

    // arrange
    var request = addressDefaultRequest();

    // act
    var address = service.create(PEOPLE_ID, request);

    // assert
    verify(repository, times(1)).save(any());
    verify(peopleService, times(1)).findById(any());
    assertEquals(PEOPLE_ID, address.getPeople().getId());
    assertTrue(address.getFavorite());
    assertNotNull(address.getId());
  }

  @Test
  @DisplayName("deve criar um endereço para uma pessoa existente que ja possui endereço favorito.")
  @Sql({
      "/mocks/address/clear.sql",
      "/mocks/people/clear.sql",
      "/mocks/people/insert.sql",
      "/mocks/address/insert.sql"
  })
  void shouldCreateAnAddressWhenExistsPeopleWithAddress() throws PeopleNotFoundException {

    // arrange
    var request = addressDefaultRequest();

    // act
    var address = service.create(PEOPLE_ID, request);

    // assert
    verify(repository, times(1)).save(any());
    verify(peopleService, times(1)).findById(any());
    assertEquals(PEOPLE_ID, address.getPeople().getId());
    assertFalse(address.getFavorite());
    assertNotNull(address.getId());
  }

  @Test
  @DisplayName("não deve criar um endereço quando a pessoa não existe.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldNotCreateAnAddressWhenPeopleNotExists() throws PeopleNotFoundException {

    // arrange
    var request = addressDefaultRequest();

    // assert
    assertThrows(PeopleNotFoundException.class, () -> {
      service.create(PEOPLE_ID, request);
    });
    verify(peopleService, times(1)).findById(any());
    verify(this.repository, times(0)).save(any());
  }

  @Test
  @DisplayName("deve atualizar um endereço para favorito.")
  @Sql({
      "/mocks/address/clear.sql",
      "/mocks/people/clear.sql",
      "/mocks/people/insert.sql",
      "/mocks/address/insert.sql"
  })
  void shouldUpdateAddressToFavorite() throws PeopleNotFoundException {

    // act
    service.updateFavorite(PEOPLE_ID, ADDRESS_ID);

    // assert
    verify(peopleService, times(1)).findById(any());
    verify(repository, times(1)).findAddressByPeople(any());
    verify(repository, times(1)).saveAllAndFlush(any());
  }

  @Test
  @DisplayName("Não deve atualizar um endereço para favorito quando pessoa nao encontrada.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldNotUpdateAddressToFavoriteWhenPeopleNotFound() throws PeopleNotFoundException {

    // assert
    assertThrows(PeopleNotFoundException.class, () -> {
      service.updateFavorite(PEOPLE_ID, ADDRESS_ID);
    });

    verify(peopleService, times(1)).findById(any());
    verify(repository, times(0)).findAddressByPeople(any());
    verify(repository, times(0)).saveAllAndFlush(any());
  }
}
