package com.henr.analise_credito.useCase;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henr.analise_credito.dto.CustomerDTO;
import com.henr.analise_credito.dto.UpdateCustomerRequestDTO;
import com.henr.analise_credito.entity.Customer;
import com.henr.analise_credito.exception.CustomerNotFoundException;
import com.henr.analise_credito.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UpdateCustomerUseCase {

  @Autowired
  private CustomerRepository customerRepository;
  
  public CustomerDTO execute(UUID id, UpdateCustomerRequestDTO dto) {
    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> {
            log.warn("Customer not found for id: {}", id);
            return new CustomerNotFoundException();
          });
    
    if (dto.name() != null) customer.setName(dto.name());
    if (dto.birthDate() != null) customer.setBirthDate(dto.birthDate());

     Customer saved = customerRepository.save(customer);
      log.info("Customer updated with ID: {}", saved.getId());

      return new CustomerDTO(
          saved.getId(),
          saved.getName(),
          saved.getCpf(),
          saved.getBirthDate(),
          saved.getCreatedAt()
      );
  }
}
