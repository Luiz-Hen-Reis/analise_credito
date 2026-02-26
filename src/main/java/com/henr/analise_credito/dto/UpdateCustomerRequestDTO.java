package com.henr.analise_credito.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for updating a customer")
public record UpdateCustomerRequestDTO(

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Full name of the customer", example = "John Doe")
    String name,

    @Past(message = "Birth date must be a date in the past")
    @Schema(description = "Customer birth date", example = "1990-05-20")
    LocalDate birthDate

) {}