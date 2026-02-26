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
public class ListCustomerByCpfUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ListCustomerByCpfUseCase listCustomerByCpfUseCase;

    @Test
    void should_throw_exception_when_customer_not_found() {
        String cpf = "12345678901";

        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
            () -> listCustomerByCpfUseCase.execute(cpf));
    }

    @Test
    void should_return_customer_when_found() {
        String cpf = "12345678901";

        Customer customer = Customer.builder()
            .id(UUID.randomUUID())
            .name("John Doe")
            .cpf(cpf)
            .birthDate(LocalDate.of(1990, 5, 20))
            .createdAt(LocalDateTime.now())
            .build();

        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.of(customer));

        CustomerDTO result = listCustomerByCpfUseCase.execute(cpf);

        assertNotNull(result);
        assertEquals(customer.getId(), result.id());
        assertEquals(customer.getName(), result.name());
        assertEquals(customer.getCpf(), result.cpf());
        assertEquals(customer.getBirthDate(), result.birthDate());
    }
}