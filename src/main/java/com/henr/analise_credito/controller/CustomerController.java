package com.henr.analise_credito.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.henr.analise_credito.dto.CreateCustomerRequestDTO;
import com.henr.analise_credito.dto.CreateCustomerResponseDTO;
import com.henr.analise_credito.dto.CustomerDTO;
import com.henr.analise_credito.exception.ErrorResponse;
import com.henr.analise_credito.useCase.CreateCustomerUseCase;
import com.henr.analise_credito.useCase.DeleteCustomerUseCase;
import com.henr.analise_credito.useCase.ListAllCustomersUseCase;
import com.henr.analise_credito.useCase.ListCustomerByCpfUseCase;
import com.henr.analise_credito.useCase.ListCustomerByIdUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Customer management")
@RequiredArgsConstructor
public class CustomerController {

    private final ListAllCustomersUseCase listAllCustomersUseCase;
    private final ListCustomerByCpfUseCase listCustomerByCpfUseCase;
    private final ListCustomerByIdUseCase listCustomerByIdUseCase;
    private final CreateCustomerUseCase createCustomerUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;

    @GetMapping
    @Operation(
        summary = "Find all customers",
        responses = {
            @ApiResponse(responseCode = "200", description = "Customers found",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerDTO.class))),
        }
    )
    public ResponseEntity<List<CustomerDTO>> listAllCustomers() {
        List<CustomerDTO> customers = listAllCustomersUseCase.execute();
        return ResponseEntity.ok(customers);
    }

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

    @PostMapping
    @Operation(
        summary = "Create a new customer",
        responses = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateCustomerResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Customer already exists",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<CreateCustomerResponseDTO> CreateCustomer(@RequestBody CreateCustomerRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createCustomerUseCase.execute(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a customer",
        responses = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        deleteCustomerUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}