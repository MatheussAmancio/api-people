package com.apipessoas.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table(name = "PEOPLE")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class People {

  @Id
  @GeneratedValue(generator = "uuid")
  @Column(name = "IDT_PEOPLE", columnDefinition = "uuid")
  private UUID id;

  @Column(name = "NAME", nullable = false)
  @NonNull
  private String name;

  @Column(name = "DATE_OF_BIRTH", nullable = false)
  @NonNull
  private Date dateOfBirth;

  @OneToMany(fetch = FetchType.EAGER)
  @Setter
  @JoinColumn(name = "people", referencedColumnName = "IDT_PEOPLE")
  @JsonManagedReference
  private List<Address> address;

  @ToString.Exclude
  @CreationTimestamp
  @Column(name = "DAT_CREATED")
  private LocalDateTime createdAt;

  @ToString.Exclude
  @UpdateTimestamp
  @Column(name = "DAT_UPDATED")
  private LocalDateTime updatedAt;
}
