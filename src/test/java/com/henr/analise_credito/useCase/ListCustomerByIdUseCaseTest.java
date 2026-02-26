package com.henr.analise_credito.useCase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.henr.analise_credito.dto.CustomerDTO;
import com.henr.analise_credito.entity.Customer;
import com.henr.analise_credito.exception.CustomerNotFoundException;
import com.henr.analise_credito.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class ListCustomerByIdUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ListCustomerByIdUseCase listCustomerByIdUseCase;

    @Test
    void should_throw_exception_when_customer_not_found() {
        UUID id = UUID.randomUUID();

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
            () -> listCustomerByIdUseCase.execute(id));
    }

    @Test
    void should_return_customer_when_found() {
        UUID id = UUID.randomUUID();

        Customer customer = Customer.builder()
            .id(id)
            .name("John Doe")
            .cpf("12345678901")
            .birthDate(LocalDate.of(1990, 5, 20))
            .createdAt(LocalDateTime.now())
            .build();

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        CustomerDTO result = listCustomerByIdUseCase.execute(id);

        assertNotNull(result);
        assertEquals(customer.getId(), result.id());
        assertEquals(customer.getName(), result.name());
        assertEquals(customer.getCpf(), result.cpf());
        assertEquals(customer.getBirthDate(), result.birthDate());
    }
}