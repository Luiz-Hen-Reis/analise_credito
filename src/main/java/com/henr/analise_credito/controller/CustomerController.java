package com.henr.analise_credito.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.henr.analise_credito.dto.CustomerDTO;
import com.henr.analise_credito.exception.ErrorResponse;
import com.henr.analise_credito.useCase.ListCustomerByCpfUseCase;
import com.henr.analise_credito.useCase.ListCustomerByIdUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Customer management")
@RequiredArgsConstructor
public class CustomerController {

    private final ListCustomerByCpfUseCase listCustomerByCpfUseCase;
    private final ListCustomerByIdUseCase listCustomerByIdUseCase;

    @GetMapping("/cpf/{cpf}")
    @Operation(
        summary = "Find customer by CPF",
        responses = {
            @ApiResponse(responseCode = "200", description = "Customer found",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<CustomerDTO> listCustomerByCpf(@PathVariable String cpf) {
        CustomerDTO customer = listCustomerByCpfUseCase.execute(cpf);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Find customer by ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Customer found",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<CustomerDTO> listCustomerById(@PathVariable UUID id) {
        CustomerDTO customer = listCustomerByIdUseCase.execute(id);
        return ResponseEntity.ok(customer);
    }
}