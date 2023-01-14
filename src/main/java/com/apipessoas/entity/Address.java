package com.apipessoas.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "ADDRESS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Address {

  @Id
  @GeneratedValue
  @Column(name = "IDT_ADDRESS", columnDefinition = "uuid")
  private UUID id;

  @Column(name = "STREET", nullable = false)
  @NonNull
  private String street;

  @Column(name = "CEP", nullable = false)
  @NonNull
  private String cep;

  @Column(name = "NUMBER", nullable = false)
  @NonNull
  private String number;

  @Column(name = "CITY", nullable = false)
  @NonNull
  private String city;

  @Column(name = "FAVORITE", nullable = false)
  @NonNull
  @Setter
  private Boolean favorite;

  @ManyToOne(fetch = FetchType.EAGER)
  @JsonBackReference
  private People people;

  @ToString.Exclude
  @CreationTimestamp
  @Column(name = "DAT_CREATED")
  private LocalDateTime createdAt;

  @ToString.Exclude
  @UpdateTimestamp
  @Column(name = "DAT_UPDATED")
  private LocalDateTime updatedAt;
}
