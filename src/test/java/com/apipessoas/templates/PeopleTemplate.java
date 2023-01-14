package com.apipessoas.templates;

import com.apipessoas.controller.dto.PeopleRequest;
import com.apipessoas.entity.People;
import com.apipessoas.utils.DateUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class PeopleTemplate {

  public static PeopleRequest peopleDefaultRequest() {
    return PeopleRequest.builder().name("Nome mock test").dateOfBirth("2023-01-12").build();
  }

  public static PeopleRequest peopleRequestWithoutName() {
    return PeopleRequest.builder().dateOfBirth("2023-01-12").build();
  }

  public static PeopleRequest peopleRequestWithIncorrectDateOfBirth() {
    return PeopleRequest.builder().name("Nome mock test").dateOfBirth("01-12-2023").build();
  }

  public static People peopleDefault() {

    return People.builder()
        .id(UUID.fromString("c7078f22-2aad-4578-928c-974db4cd7b89"))
        .name("Nome mock test")
        .dateOfBirth(DateUtils.dateFromString("2023-01-12")).build();
  }

  public static Page<People> pagePeopleDefault() {

    List<People> peoples = List.of(
        People.builder()
            .id(UUID.fromString("c7078f22-2aad-4578-928c-974db4cd7b89"))
            .name("Nome mock test")
            .dateOfBirth(DateUtils.dateFromString("2023-01-12")).build(),
        People.builder()
            .id(UUID.fromString("9a6db26a-252d-4f3b-b60a-bec6c9342492"))
            .name("Nome mock test")
            .dateOfBirth(DateUtils.dateFromString("2023-01-12")).build()
    );

    return new PageImpl<>(peoples);
  }
}
