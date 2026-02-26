package com.henr.analise_credito.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.henr.analise_credito.dto.CustomerDTO;
import com.henr.analise_credito.exception.ErrorResponse;
import com.henr.analise_credito.useCase.ListCustomerByCpfUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Gerenciamento de clientes")
@RequiredArgsConstructor
public class CustomerController {
  
   private final ListCustomerByCpfUseCase listCustomerByCpfUseCase;

  @GetMapping("/{cpf}")
  @Operation(
      summary = "Buscar cliente por CPF",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Cliente encontrado",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = CustomerDTO.class)
              )
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Cliente não encontrado",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )
      }
  )
  public ResponseEntity<CustomerDTO> listCustomerByCpf(@Valid String cpf) {
    CustomerDTO customer = listCustomerByCpfUseCase.execute(cpf);

    return ResponseEntity.ok().body(customer);
  }

}
