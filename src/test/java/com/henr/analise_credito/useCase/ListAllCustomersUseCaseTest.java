package com.henr.analise_credito.useCase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.henr.analise_credito.dto.CustomerDTO;
import com.henr.analise_credito.entity.Customer;
import com.henr.analise_credito.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class ListAllCustomersUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ListAllCustomersUseCase listAllCustomersUseCase;

    @Test
    void should_return_empty_list_when_no_customers() {
        when(customerRepository.findAll()).thenReturn(List.of());

        List<CustomerDTO> result = listAllCustomersUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(customerRepository).findAll();
    }

    @Test
    void should_return_mapped_customers() {
        Customer customer1 = Customer.builder()
            .id(UUID.randomUUID())
            .name("John Doe")
            .cpf("12345678901")
            .birthDate(LocalDate.of(1990, 5, 20))
            .createdAt(LocalDateTime.now())
            .build();

        Customer customer2 = Customer.builder()
            .id(UUID.randomUUID())
            .name("Jane Doe")
            .cpf("98765432100")
            .birthDate(LocalDate.of(1995, 3, 15))
            .createdAt(LocalDateTime.now())
            .build();

        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        List<CustomerDTO> result = listAllCustomersUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(customer1.getId(), result.get(0).id());
        assertEquals(customer1.getName(), result.get(0).name());
        assertEquals(customer1.getCpf(), result.get(0).cpf());

        assertEquals(customer2.getId(), result.get(1).id());
        assertEquals(customer2.getName(), result.get(1).name());
        assertEquals(customer2.getCpf(), result.get(1).cpf());
    }
}