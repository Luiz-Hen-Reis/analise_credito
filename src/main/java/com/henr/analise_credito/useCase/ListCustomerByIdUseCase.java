package com.henr.analise_credito.useCase;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henr.analise_credito.dto.CustomerDTO;
import com.henr.analise_credito.entity.Customer;
import com.henr.analise_credito.exception.CustomerNotFoundException;
import com.henr.analise_credito.repository.CustomerRepository;

@Service
public class ListCustomerByIdUseCase {

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerDTO execute(UUID id) {
        Customer customer = customerRepository.findById(id)
          .orElseThrow(() -> new CustomerNotFoundException());

        return new CustomerDTO(
            customer.getId(),
            customer.getName(),
            customer.getCpf(),
            customer.getBirthDate(),
            customer.getCreatedAt()
        );
    }
}
