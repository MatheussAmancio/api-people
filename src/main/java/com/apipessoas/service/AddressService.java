package com.apipessoas.service;

import com.apipessoas.controller.dto.AddressRequest;
import com.apipessoas.controller.exception.AddressNotFoundException;
import com.apipessoas.controller.exception.PeopleNotFoundException;
import com.apipessoas.entity.Address;
import com.apipessoas.repository.AddressRepository;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddressService {

  private final AddressRepository repository;

  private final PeopleService peopleService;

  @Autowired
  public AddressService(final AddressRepository repository, final PeopleService peopleService) {
    this.repository = repository;
    this.peopleService = peopleService;
  }

  @Transactional
  public Address create(final UUID peopleId, final AddressRequest request)
      throws PeopleNotFoundException {
    try {
      var people = this.peopleService.findById(peopleId);

      var address = Address.builder()
          .people(people)
          .street(request.getStreet())
          .cep(request.getCep())
          .number(request.getNumber())
          .city(request.getCity())
          .favorite(people.getAddress().isEmpty())
          .build();

      var addressSaved = this.repository.save(address);
      this.peopleService.setAddress(people, address);

      return addressSaved;
    } catch (Exception e) {
      log.error("m=createAddress, error={}", e.getMessage());
      throw e;
    }
  }

  public void updateFavorite(final UUID peopleId, final UUID addressId)
      throws PeopleNotFoundException {
    try {
      var people = this.peopleService.findById(peopleId);

      var addresses = this.repository.findAddressByPeople(people);
      addresses.forEach(address -> {
        address.setFavorite(address.getId().equals(addressId));
      });

      this.repository.saveAllAndFlush(addresses);
    } catch (Exception e) {
      log.error("m=updateFavorite, error={}", e.getMessage());
      throw e;
    }
  }
}
