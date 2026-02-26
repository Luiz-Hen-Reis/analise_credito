package com.henr.analise_credito.useCase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.henr.analise_credito.dto.CustomerDTO;
import com.henr.analise_credito.dto.UpdateCustomerRequestDTO;
import com.henr.analise_credito.entity.Customer;
import com.henr.analise_credito.exception.CustomerNotFoundException;
import com.henr.analise_credito.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class UpdateCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private UpdateCustomerUseCase updateCustomerUseCase;

    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    @Test
    void should_throw_exception_when_customer_not_found() {
        UUID id = UUID.randomUUID();
        UpdateCustomerRequestDTO dto = new UpdateCustomerRequestDTO("New Name", null);

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
            () -> updateCustomerUseCase.execute(id, dto));

        verify(customerRepository, never()).save(any());
    }

    @Test
    void should_update_name_only_when_birthdate_is_null() {
        UUID id = UUID.randomUUID();
        LocalDate originalBirthDate = LocalDate.of(1990, 5, 20);
        UpdateCustomerRequestDTO dto = new UpdateCustomerRequestDTO("Updated Name", null);

        Customer customer = Customer.builder()
            .id(id)
            .name("John Doe")
            .cpf("12345678901")
            .birthDate(originalBirthDate)
            .createdAt(LocalDateTime.now())
            .build();

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = updateCustomerUseCase.execute(id, dto);

        assertNotNull(result);
        verify(customerRepository).save(customerCaptor.capture());

        Customer captured = customerCaptor.getValue();
        assertEquals("Updated Name", captured.getName());
        assertEquals(originalBirthDate, captured.getBirthDate());
    }

    @Test
    void should_update_all_fields_when_all_provided() {
        UUID id = UUID.randomUUID();
        LocalDate newBirthDate = LocalDate.of(1995, 8, 10);
        UpdateCustomerRequestDTO dto = new UpdateCustomerRequestDTO("Updated Name", newBirthDate);

        Customer customer = Customer.builder()
            .id(id)
            .name("John Doe")
            .cpf("12345678901")
            .birthDate(LocalDate.of(1990, 5, 20))
            .createdAt(LocalDateTime.now())
            .build();

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = updateCustomerUseCase.execute(id, dto);

        assertNotNull(result);
        verify(customerRepository).save(customerCaptor.capture());

        Customer captured = customerCaptor.getValue();
        assertEquals("Updated Name", captured.getName());
        assertEquals(newBirthDate, captured.getBirthDate());
    }
}