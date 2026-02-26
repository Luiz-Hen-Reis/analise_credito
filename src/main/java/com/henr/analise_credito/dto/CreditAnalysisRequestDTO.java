package com.henr.analise_credito.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request body for credit analysis")
public record CreditAnalysisRequestDTO(

    @NotBlank(message = "Customer CPF is required")
    @Schema(description = "Customer CPF", example = "12345678901")
    String cpf,

    @NotNull(message = "Monthly income is required")
    @Positive(message = "Monthly income must be positive")
    @Schema(description = "Customer monthly income", example = "5000.00")
    BigDecimal monthlyIncome,

    @NotNull(message = "Requested amount is required")
    @Positive(message = "Requested amount must be positive")
    @Schema(description = "Amount requested", example = "20000.00")
    BigDecimal requestedAmount,

    @NotNull(message = "Installments is required")
    @Min(value = 1, message = "Minimum 1 installment")
    @Schema(description = "Number of installments", example = "24")
    Integer installments,

    @NotNull(message = "Credit score is required")
    @Schema(description = "Customer credit score (0-1000)", example = "700")
    Integer creditScore,

    @NotNull(message = "Recent queries is required")
    @Schema(description = "Number of credit queries in last 30 days", example = "2")
    Integer recentQueries

) {}