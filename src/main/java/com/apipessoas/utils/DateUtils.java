package com.apipessoas.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.SneakyThrows;

public class DateUtils {

  @SneakyThrows
  public static Date dateFromString(String date) {
    return new SimpleDateFormat("yyyy-MM-dd").parse(date);
  }
}
