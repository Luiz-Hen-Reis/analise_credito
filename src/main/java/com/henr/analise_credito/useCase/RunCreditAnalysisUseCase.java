package com.henr.analise_credito.useCase;

import org.springframework.stereotype.Service;

import com.henr.analise_credito.credit.engine.CreditEngine;
import com.henr.analise_credito.credit.model.AnalysisResult;
import com.henr.analise_credito.credit.model.CreditRequest;
import com.henr.analise_credito.dto.CreditAnalysisRequestDTO;
import com.henr.analise_credito.dto.CreditAnalysisResponseDTO;
import com.henr.analise_credito.entity.CreditAnalysis;
import com.henr.analise_credito.entity.Customer;
import com.henr.analise_credito.exception.CustomerNotFoundException;
import com.henr.analise_credito.repository.CreditAnalysisRepository;
import com.henr.analise_credito.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RunCreditAnalysisUseCase {
  
  private final CustomerRepository customerRepository;
  private final CreditAnalysisRepository creditAnalysisRepository;
  private final CreditEngine creditEngine;

  public CreditAnalysisResponseDTO execute(CreditAnalysisRequestDTO dto) {
        Customer customer = customerRepository.findByCpf(dto.cpf())
          .orElseThrow(() -> {
            log.warn("Customer not found for cpf: {}", dto.cpf());
            return new CustomerNotFoundException();
          });

        log.info("Running credit analysis for customer: {} - CPF: {}", customer.getName(), dto.cpf());

        CreditRequest request = CreditRequest.builder()
            .cpf(customer.getCpf())
            .name(customer.getName())
            .birthDate(customer.getBirthDate())
            .monthlyIncome(dto.monthlyIncome())
            .requestedAmount(dto.requestedAmount())
            .installments(dto.installments())
            .creditScore(dto.creditScore())
            .recentQueries(dto.recentQueries())
            .build();

        AnalysisResult result = creditEngine.process(request);

        CreditAnalysis analysis = CreditAnalysis.builder()
            .customer(customer)
            .calculatedScore(result.getCalculatedScore())
            .reportedIncome(dto.monthlyIncome())
            .result(result.isApproved() ? "APPROVED" : "DENIED")
            .detailedReason(result.getReason())
            .build();

        CreditAnalysis saved = creditAnalysisRepository.save(analysis);
        log.info("Credit analysis saved with ID: {} - Result: {}", saved.getId(), saved.getResult());

        return new CreditAnalysisResponseDTO(
            saved.getId(),
            customer.getName(),
            customer.getCpf(),
            result.isApproved(),
            result.getCalculatedScore(),
            result.getReason(),
            saved.getProcessedAt()
        );
    }
}
