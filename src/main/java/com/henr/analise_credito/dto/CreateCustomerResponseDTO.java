package com.henr.analise_credito.dto;

import java.util.UUID;

public record CreateCustomerResponseDTO(
    UUID id,
    String message
) {}