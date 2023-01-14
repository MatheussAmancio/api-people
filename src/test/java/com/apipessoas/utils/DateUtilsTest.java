package com.apipessoas.utils;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DateUtilsTest {

  @Test
  @DisplayName("Deve converter uma string em data quando o formato corresponder a yyyy-MM-dd")
  void shouldConvertStringToDateWhenCorrectFormat() {
    var date = DateUtils.dateFromString("2023-01-12");
    assertInstanceOf(Date.class, date);
  }

  @Test
  @DisplayName("Deve lançar uma exception quando o formato não corresponder a yyyy-MM-dd")
  void shouldThrowWhenNotCorrectFormat() {
    assertThrows(
        Exception.class,
        () -> {
          DateUtils.dateFromString("2023/01/12");
        });
  }
}
