package com.henr.analise_credito.useCase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.henr.analise_credito.credit.engine.CreditEngine;
import com.henr.analise_credito.credit.model.AnalysisResult;
import com.henr.analise_credito.dto.CreditAnalysisRequestDTO;
import com.henr.analise_credito.dto.CreditAnalysisResponseDTO;
import com.henr.analise_credito.entity.CreditAnalysis;
import com.henr.analise_credito.entity.Customer;
import com.henr.analise_credito.exception.CustomerNotFoundException;
import com.henr.analise_credito.repository.CreditAnalysisRepository;
import com.henr.analise_credito.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
public class RunCreditAnalysisUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CreditAnalysisRepository creditAnalysisRepository;

    @Mock
    private CreditEngine creditEngine;

    @InjectMocks
    private RunCreditAnalysisUseCase runCreditAnalysisUseCase;

    @Captor
    private ArgumentCaptor<CreditAnalysis> analysisCaptor;

    private CreditAnalysisRequestDTO dto;
    private Customer customer;

    @BeforeEach
    void setup() {
        dto = new CreditAnalysisRequestDTO(
            "12345678901",
            new BigDecimal("5000.00"),
            new BigDecimal("20000.00"),
            24,
            700,
            2
        );

        customer = Customer.builder()
            .id(UUID.randomUUID())
            .name("John Doe")
            .cpf("12345678901")
            .birthDate(LocalDate.of(1990, 5, 20))
            .createdAt(LocalDateTime.now())
            .build();
    }

    @Test
    void should_throw_exception_when_customer_not_found() {
        when(customerRepository.findByCpf(dto.cpf())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
            () -> runCreditAnalysisUseCase.execute(dto));

        verify(creditEngine, never()).process(any());
        verify(creditAnalysisRepository, never()).save(any());
    }

    @Test
    void should_save_and_return_approved_analysis() {
        CreditAnalysis savedAnalysis = CreditAnalysis.builder()
            .id(UUID.randomUUID())
            .customer(customer)
            .calculatedScore(750)
            .reportedIncome(dto.monthlyIncome())
            .result("APPROVED")
            .detailedReason("All rules passed successfully")
            .processedAt(LocalDateTime.now())
            .build();

        when(customerRepository.findByCpf(dto.cpf())).thenReturn(Optional.of(customer));
        when(creditEngine.process(any())).thenReturn(AnalysisResult.approve(750));
        when(creditAnalysisRepository.save(any())).thenReturn(savedAnalysis);

        CreditAnalysisResponseDTO response = runCreditAnalysisUseCase.execute(dto);

        assertNotNull(response);
        assertTrue(response.approved());
        assertEquals(750, response.calculatedScore());
        assertEquals("All rules passed successfully", response.reason());
        assertEquals(customer.getName(), response.customerName());
        assertEquals(customer.getCpf(), response.cpf());

        verify(creditAnalysisRepository).save(analysisCaptor.capture());
        CreditAnalysis captured = analysisCaptor.getValue();
        assertEquals("APPROVED", captured.getResult());
        assertEquals(750, captured.getCalculatedScore());
    }

    @Test
    void should_save_and_return_denied_analysis() {
        CreditAnalysis savedAnalysis = CreditAnalysis.builder()
            .id(UUID.randomUUID())
            .customer(customer)
            .calculatedScore(0)
            .reportedIncome(dto.monthlyIncome())
            .result("DENIED")
            .detailedReason("Credit score is below the minimum required score of 600.")
            .processedAt(LocalDateTime.now())
            .build();

        when(customerRepository.findByCpf(dto.cpf())).thenReturn(Optional.of(customer));
        when(creditEngine.process(any()))
            .thenReturn(AnalysisResult.deny("Credit score is below the minimum required score of 600."));
        when(creditAnalysisRepository.save(any())).thenReturn(savedAnalysis);

        CreditAnalysisResponseDTO response = runCreditAnalysisUseCase.execute(dto);

        assertNotNull(response);
        assertFalse(response.approved());
        assertEquals(0, response.calculatedScore());
        assertEquals("Credit score is below the minimum required score of 600.", response.reason());

        verify(creditAnalysisRepository).save(analysisCaptor.capture());
        CreditAnalysis captured = analysisCaptor.getValue();
        assertEquals("DENIED", captured.getResult());
    }
}