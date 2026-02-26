package com.henr.analise_credito.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credit analysis result")
public record CreditAnalysisResponseDTO(
    UUID id,
    String customerName,
    String cpf,
    boolean approved,
    int calculatedScore,
    String reason,
    LocalDateTime processedAt
) {}