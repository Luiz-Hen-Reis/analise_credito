package com.henr.analise_credito.useCase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.henr.analise_credito.dto.CreateCustomerRequestDTO;
import com.henr.analise_credito.dto.CreateCustomerResponseDTO;
import com.henr.analise_credito.entity.Customer;
import com.henr.analise_credito.exception.ResourceAlreadyExistsException;
import com.henr.analise_credito.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class CreateCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CreateCustomerUseCase createCustomerUseCase;

    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    private CreateCustomerRequestDTO dto;

    @BeforeEach
    void setup() {
        dto = new CreateCustomerRequestDTO(
            "John Doe",
            "12345678901",
            LocalDate.of(1990, 5, 20)
        );
    }

    @Test
    void should_throw_exception_when_customer_already_exists() {
        when(customerRepository.findByCpf(dto.cpf()))
            .thenReturn(Optional.of(new Customer()));

        assertThrows(ResourceAlreadyExistsException.class,
            () -> createCustomerUseCase.execute(dto));

        verify(customerRepository, never()).save(any());
    }

    @Test
    void should_create_customer_and_return_response() {
        when(customerRepository.findByCpf(dto.cpf()))
            .thenReturn(Optional.empty());

        Customer savedCustomer = Customer.builder()
            .id(UUID.randomUUID())
            .name(dto.name())
            .cpf(dto.cpf())
            .birthDate(dto.birthDate())
            .build();

        when(customerRepository.save(any(Customer.class)))
            .thenReturn(savedCustomer);

        CreateCustomerResponseDTO response = createCustomerUseCase.execute(dto);

        assertNotNull(response);
        assertEquals(savedCustomer.getId(), response.id());
        assertEquals("Customer created successfully", response.message());

        verify(customerRepository).save(customerCaptor.capture());

        Customer capturedCustomer = customerCaptor.getValue();
        assertEquals(dto.name(), capturedCustomer.getName());
        assertEquals(dto.cpf(), capturedCustomer.getCpf());
        assertEquals(dto.birthDate(), capturedCustomer.getBirthDate());
    }
}