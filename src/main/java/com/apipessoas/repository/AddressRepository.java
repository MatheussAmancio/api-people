package com.apipessoas.repository;

import com.apipessoas.entity.Address;
import com.apipessoas.entity.People;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

  List<Address> findAddressByPeople(People people);

}
