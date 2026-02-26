package com.henr.analise_credito.useCase;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henr.analise_credito.dto.CreateCustomerRequestDTO;
import com.henr.analise_credito.dto.CreateCustomerResponseDTO;
import com.henr.analise_credito.entity.Customer;
import com.henr.analise_credito.exception.ResourceAlreadyExistsException;
import com.henr.analise_credito.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CreateCustomerUseCase {
  
  @Autowired
  private CustomerRepository customerRepository;

  public CreateCustomerResponseDTO execute(CreateCustomerRequestDTO dto) {
    Optional<Customer> existingCustomer = customerRepository.findByCpf(dto.cpf());
      
    if (existingCustomer.isPresent()) {
      log.warn("Customer already exists with CPF: {}", dto.cpf());
      throw new ResourceAlreadyExistsException("Customer", "CPF", dto.cpf());
    }

    Customer customer = Customer.builder()
      .name(dto.name())
      .cpf(dto.cpf())
      .birthDate(dto.birthDate())
      .build();

    Customer saved = customerRepository.save(customer);
    log.info("Customer created with ID: {}", saved.getId());

    return new CreateCustomerResponseDTO(
      saved.getId(),
      "Customer created successfully"
    );
  }
}
