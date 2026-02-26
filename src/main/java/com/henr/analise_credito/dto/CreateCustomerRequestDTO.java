package com.henr.analise_credito.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for creating a new customer")
public record CreateCustomerRequestDTO(

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Full name of the customer", example = "John Doe")
    String name,

    @NotBlank(message = "CPF is required")
    @CPF(message = "Invalid CPF")
    @Schema(description = "Customer CPF, numbers only", example = "12345678901")
    String cpf,

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be a date in the past")
    @Schema(description = "Customer birth date", example = "1990-05-20")
    LocalDate birthDate

) {}