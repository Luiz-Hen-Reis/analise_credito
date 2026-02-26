package com.henr.analise_credito.useCase;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henr.analise_credito.exception.CustomerNotFoundException;
import com.henr.analise_credito.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeleteCustomerUseCase {

    @Autowired
    private CustomerRepository customerRepository;

    public void execute(UUID id) {
        if (!customerRepository.existsById(id)) {
            log.warn("Customer not found for id: {}", id);
            throw new CustomerNotFoundException();
        }

        customerRepository.deleteById(id);
        log.info("Customer deleted with ID: {}", id);
    }
}