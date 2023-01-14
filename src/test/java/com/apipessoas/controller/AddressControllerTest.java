package com.apipessoas.controller;

import static com.apipessoas.templates.AddressTemplate.addressDefaultRequest;
import static com.apipessoas.templates.AddressTemplate.addressRequestWithoutCepAndCity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.apipessoas.BaseTest;
import com.apipessoas.repository.AddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AddressControllerTest extends BaseTest {

  static final String PEOPLE_ID = "c7078f22-2aad-4578-928c-974db4cd7b89";
  static final String ADDRESS_ID = "e23edd2c-0cd0-4461-a6b4-1650df33666a";
  static final String ENDPOINT = String.format("/api/v1/people/%s/address", PEOPLE_ID);

  static final String ENDPOINT_PATCH = String.format(
      "/api/v1/people/%s/address/%s",
      PEOPLE_ID,
      ADDRESS_ID
  );

  @Autowired
  private WebApplicationContext context;

  @SpyBean
  AddressRepository repository;

  private MockMvc mockMvc;
  private ObjectMapper mapper;

  @BeforeEach
  void beforeEach() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    this.mapper = new ObjectMapper();
  }

  @Test
  @DisplayName("POST /api/v1/people/{people-id}/address -> "
      + "deve retornar 200 quando requisição bem sucedida.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql", "/mocks/people/insert.sql"})
  void shouldBeCreateAndReturnOkStatusWhenPayloadIsCorrect() throws Exception {

    // arrange
    var request = this.mapper.writeValueAsString(addressDefaultRequest());

    // act
    var response =
        this.mockMvc
            .perform(post(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

    // assert
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertTrue(response.getContentAsString().contains("id"));
  }

  @Test
  @DisplayName("PATCH /api/v1/people/{people-id}/address/631aef03-b181-4750-8ce6-04ada6529aea -> "
      + "deve retornar 200 quando requisição bem sucedida.")
  @Sql({
      "/mocks/address/clear.sql",
      "/mocks/people/clear.sql",
      "/mocks/people/insert.sql",
      "/mocks/address/insert.sql"
  })
  void shouldBeUpdateReturnOkStatusWhenPayloadIsCorrect() throws Exception {

    // arrange
    var request = this.mapper.writeValueAsString(addressDefaultRequest());

    // act
    var response =
        this.mockMvc
            .perform(patch(ENDPOINT_PATCH).contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

    // assert
    assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

  @Test
  @DisplayName("POST /api/v1/people/{people-id}/address -> "
      + "deve retornar 400 quando payload incorreto.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldBeReturn4xxStatusWhenPayloadIsInCorrect() throws Exception {

    // arrange
    var request = this.mapper.writeValueAsString(addressRequestWithoutCepAndCity());

    // act
    var response =
        this.mockMvc
            .perform(post(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse();

    // assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    assertTrue(response.getContentAsString().contains("cep is required."));
    assertTrue(response.getContentAsString().contains("city is required."));
  }

  @Test
  @DisplayName("GET /api/v1/people/{id} -> deve retornar 404 quando pessoa não encontrada.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldBeReturnNotFountStatusWhenPayloadIsCorrectButPeopleNotFound() throws Exception {

    // arrange
    var request = this.mapper.writeValueAsString(addressDefaultRequest());

    // act
    var response =
        this.mockMvc
            .perform(patch(ENDPOINT_PATCH).contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().is4xxClientError())
            .andReturn()
            .getResponse();

    // assert
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    assertTrue(response.getContentAsString()
        .contains("People not found with id c7078f22-2aad-4578-928c-974db4cd7b89"));
  }

}
