package com.henr.analise_credito.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "customers")
@Table(
    name = "customers",
    indexes = {
        @Index(name = "idx_customer_cpf", columnList = "cpf")
    }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String cpf;

  @Column(nullable = false, name = "birth_date")
  private LocalDate birthDate;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
