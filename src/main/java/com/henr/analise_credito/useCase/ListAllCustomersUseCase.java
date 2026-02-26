package com.henr.analise_credito.useCase;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henr.analise_credito.dto.CustomerDTO;
import com.henr.analise_credito.entity.Customer;
import com.henr.analise_credito.repository.CustomerRepository;

@Service
public class ListAllCustomersUseCase {
  
  @Autowired
  private CustomerRepository customerRepository;

  public List<CustomerDTO> execute() {
    List<Customer> customers = customerRepository.findAll();

    return customers.stream()
    .map(customer -> new CustomerDTO(
        customer.getId(),
        customer.getName(),
        customer.getCpf(),
        customer.getBirthDate(),
        customer.getCreatedAt()
    ))
    .toList();
  }
}
