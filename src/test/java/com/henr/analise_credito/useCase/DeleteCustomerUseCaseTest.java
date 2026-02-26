package com.henr.analise_credito.useCase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.henr.analise_credito.exception.CustomerNotFoundException;
import com.henr.analise_credito.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class DeleteCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private DeleteCustomerUseCase deleteCustomerUseCase;

    @Test
    void should_throw_exception_when_customer_not_found() {
        UUID id = UUID.randomUUID();

        when(customerRepository.existsById(id)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class,
            () -> deleteCustomerUseCase.execute(id));

        verify(customerRepository, never()).deleteById(id);
    }

    @Test
    void should_delete_customer_when_exists() {
        UUID id = UUID.randomUUID();

        when(customerRepository.existsById(id)).thenReturn(true);

        deleteCustomerUseCase.execute(id);

        verify(customerRepository).deleteById(id);
    }
}