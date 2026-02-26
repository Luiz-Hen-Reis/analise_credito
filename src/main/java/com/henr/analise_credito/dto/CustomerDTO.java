package com.henr.analise_credito.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerDTO(
  UUID id,
  String name,
  String cpf,
  LocalDate birthDate,
  LocalDateTime createdAt
) {}
