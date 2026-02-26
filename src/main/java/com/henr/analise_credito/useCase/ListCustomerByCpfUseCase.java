package com.henr.analise_credito.useCase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henr.analise_credito.dto.CustomerDTO;
import com.henr.analise_credito.entity.Customer;
import com.henr.analise_credito.exception.CustomerNotFoundException;
import com.henr.analise_credito.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ListCustomerByCpfUseCase {

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerDTO execute(String cpf) {
        Customer customer = customerRepository.findByCpf(cpf)
          .orElseThrow(() -> {
            log.warn("Customer not found for cpf: {}", cpf);
            return new CustomerNotFoundException();
          });

        return new CustomerDTO(
            customer.getId(),
            customer.getName(),
            customer.getCpf(),
            customer.getBirthDate(),
            customer.getCreatedAt()
        );
    }
}
