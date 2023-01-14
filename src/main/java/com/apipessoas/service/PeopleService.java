package com.apipessoas.service;

import com.apipessoas.controller.dto.PeopleRequest;
import com.apipessoas.controller.dto.PeopleResponse;
import com.apipessoas.controller.exception.PeopleNotFoundException;
import com.apipessoas.entity.Address;
import com.apipessoas.entity.People;
import com.apipessoas.repository.PeopleRepository;
import com.apipessoas.utils.DateUtils;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PeopleService {

  private final PeopleRepository repository;

  @Autowired
  public PeopleService(final PeopleRepository repository) {
    this.repository = repository;
  }

  public People create(final PeopleRequest request) {
    try {
      var people =
          People.builder()
              .name(request.getName())
              .dateOfBirth(DateUtils.dateFromString(request.getDateOfBirth()))
              .build();

      return this.repository.save(people);
    } catch (Exception e) {
      log.error("m=createPeople, error={}", e.getMessage());
      throw e;
    }
  }

  public People update(final UUID id, final PeopleRequest request) throws PeopleNotFoundException {
    try {

      var existsPeople = findById(id);

      var people =
          People.builder()
              .id(existsPeople.getId())
              .name(Objects.nonNull(request.getName()) ? request.getName() : existsPeople.getName())
              .dateOfBirth(Objects.nonNull(request.getDateOfBirth()) ? DateUtils.dateFromString(
                  request.getDateOfBirth()) : existsPeople.getDateOfBirth())
              .build();

      return this.repository.save(people);
    } catch (Exception e) {
      log.error("m=update, error={}", e.getMessage());
      throw e;
    }
  }

  public void setAddress(final People people, final Address address) {
    try {
      var addresses = new ArrayList<Address>(people.getAddress());
      addresses.add(address);

      people.setAddress(addresses);
      this.repository.saveAndFlush(people);
    } catch (Exception e) {
      log.error("m=setAddress, error={}", e.getMessage());
      throw e;
    }
  }

  public People find(final UUID id) throws PeopleNotFoundException {
    try {
      return findById(id);
    } catch (Exception e) {
      log.error("m=find, error={}", e.getMessage());
      throw e;
    }
  }

  public Page<PeopleResponse> list(Pageable pageable) {
    try {
      var peoples = repository.findAll(pageable);

      return toPeopleResponse(peoples);
    } catch (Exception e) {
      log.error("m=list, error={}", e.getMessage());
      throw e;
    }
  }

  private Page<PeopleResponse> toPeopleResponse(Page<People> peoples) {

    return peoples.map(new Function<People, PeopleResponse>() {
      @Override
      public PeopleResponse apply(People people) {
        return PeopleResponse.builder()
            .id(people.getId())
            .name(people.getName())
            .dateOfBirth(people.getDateOfBirth())
            .build();
      }
    });
  }

  public People findById(final UUID id) throws PeopleNotFoundException {
    return this.repository.findById(id).orElseThrow(() -> new PeopleNotFoundException(id));
  }
}
