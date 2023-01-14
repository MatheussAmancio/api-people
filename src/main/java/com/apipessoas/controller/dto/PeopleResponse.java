package com.apipessoas.controller.dto;

import com.apipessoas.entity.Address;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PeopleResponse {

  private UUID id;

  @JsonInclude(Include.NON_NULL)
  private String name;

  @JsonInclude(Include.NON_NULL)
  private Date dateOfBirth;

  @JsonInclude(Include.NON_NULL)
  private List<Address> address;
}
