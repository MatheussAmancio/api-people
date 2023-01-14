package com.apipessoas.controller;

import static com.apipessoas.templates.PeopleTemplate.pagePeopleDefault;
import static com.apipessoas.templates.PeopleTemplate.peopleDefault;
import static com.apipessoas.templates.PeopleTemplate.peopleDefaultRequest;
import static com.apipessoas.templates.PeopleTemplate.peopleRequestWithIncorrectDateOfBirth;
import static com.apipessoas.templates.PeopleTemplate.peopleRequestWithoutName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.apipessoas.BaseTest;
import com.apipessoas.controller.dto.PeopleResponse;
import com.apipessoas.repository.PeopleRepository;
import com.apipessoas.service.PeopleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PeopleControllerTest extends BaseTest {

  @SpyBean
  private PeopleService service;

  @SpyBean
  PeopleRepository repository;

  static final String PEOPLE_ID = "c7078f22-2aad-4578-928c-974db4cd7b89";
  static final String ENDPOINT_ID = String.format("/api/v1/people/%s", PEOPLE_ID);
  static final String ENDPOINT = "/api/v1/people";

  @Autowired
  private WebApplicationContext context;
  private MockMvc mockMvc;
  private ObjectMapper mapper;

  @BeforeEach
  void beforeEach() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    this.mapper = new ObjectMapper();
  }

  @Test
  @DisplayName("POST /api/v1/people -> deve retornar 200 quando requisição bem sucedida.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldBeReturnOkStatusWhenPayloadIsCorrect() throws Exception {

    // arrange
    var request = this.mapper.writeValueAsString(peopleDefaultRequest());

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
  @DisplayName("POST /api/v1/people -> deve retornar 400 quando payload incorreto.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldBeReturn4xxStatusWhenPayloadIsInCorrect() throws Exception {

    // arrange
    var request = this.mapper.writeValueAsString(peopleRequestWithoutName());

    // act
    var response =
        this.mockMvc
            .perform(post(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse();

    // assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    assertTrue(response.getContentAsString().contains("name is required."));
  }

  @Test
  @DisplayName("POST /api/v1/people -> deve retornar 400 para dataOfBirth com formato incorreto.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldBeReturn4xxStatusWhenDateOfBirthIsIncorrectPattern() throws Exception {

    // arrange
    var request = this.mapper.writeValueAsString(peopleRequestWithIncorrectDateOfBirth());

    // act
    var response =
        this.mockMvc
            .perform(post(ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().is4xxClientError())
            .andReturn()
            .getResponse();

    // assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    assertTrue(response.getContentAsString().contains("date_of_birth mus be yyyy-MM-dd."));
  }

  @Test
  @DisplayName("GET /api/v1/people/{id} -> deve retornar 200 quando requisição bem sucedida.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldBeReturnOkStatusWhenPeopleIsRemoved() throws Exception {

    // arrange
    var peopleRequest = peopleDefaultRequest();
    var request = this.mapper.writeValueAsString(peopleRequest);

    doReturn(Optional.ofNullable(peopleDefault())).when(repository)
        .findById(UUID.fromString(PEOPLE_ID));

    // act
    var response =
        this.mockMvc
            .perform(get(ENDPOINT_ID).contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

    var objectMapper = new ObjectMapper();
    var mapResponse = objectMapper.readValue(response.getContentAsString(), PeopleResponse.class);

    // assert
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertNotNull(response.getContentAsString());
    assertEquals(UUID.fromString(PEOPLE_ID), mapResponse.getId());
  }

  @Test
  @DisplayName("GET /api/v1/people/{id} -> deve retornar 404 quando pessoa não encontrada.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldBeReturnNotFountStatusWhenPayloadIsCorrectButPeopleNotFound() throws Exception {

    // arrange
    doReturn(Optional.empty()).when(repository).findById(UUID.fromString(PEOPLE_ID));

    // act
    var response =
        this.mockMvc
            .perform(get(ENDPOINT_ID).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse();

    // assert
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    assertTrue(response.getContentAsString()
        .contains("People not found with id c7078f22-2aad-4578-928c-974db4cd7b89"));
  }

  @Test
  @DisplayName("GET /api/v1/people -> deve retornar 200 e a lista de pessoas quando bem sucedido.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldBeReturnOkAndAListStatusWhenPeopleIsRemoved() throws Exception {

    // arrange
    ArgumentCaptor<Pageable> pageableCaptor =
        ArgumentCaptor.forClass(Pageable.class);

    doReturn(pagePeopleDefault()).when(repository)
        .findAll(pageableCaptor.capture());

    // act
    var response =
        this.mockMvc
            .perform(get(ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10")
                .param("sort", "id,desc")
                .param("sort", "name,asc"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

    // assert
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertNotNull(response.getContentAsString());
    assertTrue(response.getContentAsString().contains("c7078f22-2aad-4578-928c-974db4cd7b89"));
    assertTrue(response.getContentAsString().contains("9a6db26a-252d-4f3b-b60a-bec6c9342492"));
    assertTrue(response.getContentAsString().contains("totalPages"));
    assertTrue(response.getContentAsString().contains("totalElements"));
    assertTrue(response.getContentAsString().contains("first"));
    assertTrue(response.getContentAsString().contains("sort"));
  }

  @Test
  @DisplayName("PUT /api/v1/people/{id} -> deve retornar 200 quando requisição for sucedida.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldBeUpdateAndReturnOkStatusWhenPayloadIsCorrect() throws Exception {

    // arrange
    var request = this.mapper.writeValueAsString(peopleDefaultRequest());

    doReturn(Optional.ofNullable(peopleDefault())).when(repository)
        .findById(UUID.fromString(PEOPLE_ID));

    // act
    var response =
        this.mockMvc
            .perform(put(ENDPOINT_ID).contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

    // assert
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertTrue(response.getContentAsString().contains("id"));
  }

  @Test
  @DisplayName("PUT /api/v1/people/{id} -> deve retornar 400 para dataOfBirth inválido.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldBeUpdateAndReturn4xxStatusWhenDateOfBirthIsIncorrectPattern() throws Exception {

    // arrange
    var request = this.mapper.writeValueAsString(peopleRequestWithIncorrectDateOfBirth());

    // act
    var response =
        this.mockMvc
            .perform(put(ENDPOINT_ID).contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().is4xxClientError())
            .andReturn()
            .getResponse();

    // assert
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    assertTrue(response.getContentAsString().contains("date_of_birth mus be yyyy-MM-dd."));
  }

  @Test
  @DisplayName("PUT /api/v1/people/{id} -> deve retornar 404 quando pessoa não encontrada.")
  @Sql({"/mocks/address/clear.sql", "/mocks/people/clear.sql"})
  void shouldBeUpdateAndReturnNotFountStatusWhenPayloadIsCorrectButPeopleNotFound()
      throws Exception {

    // arrange
    var request = this.mapper.writeValueAsString(peopleDefaultRequest());

    doReturn(Optional.empty()).when(repository).findById(UUID.fromString(PEOPLE_ID));

    // act
    var response =
        this.mockMvc
            .perform(put(ENDPOINT_ID).contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse();

    // assert
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    assertTrue(response.getContentAsString()
        .contains("People not found with id c7078f22-2aad-4578-928c-974db4cd7b89"));
  }
}
