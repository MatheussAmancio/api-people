package com.apipessoas.repository;

import com.apipessoas.entity.People;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends JpaRepository<People, UUID> {}
