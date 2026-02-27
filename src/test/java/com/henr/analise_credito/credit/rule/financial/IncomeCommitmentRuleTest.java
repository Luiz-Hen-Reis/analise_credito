package com.henr.analise_credito.credit.rule.financial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.henr.analise_credito.credit.model.CreditRequest;

public class IncomeCommitmentRuleTest {

    private IncomeCommitmentRule incomeCommitmentRule;

    @BeforeEach
    void setup() {
        incomeCommitmentRule = new IncomeCommitmentRule();
    }

    private CreditRequest buildRequest(BigDecimal income, BigDecimal amount, int installments) {
        return CreditRequest.builder()
            .cpf("12345678901")
            .name("John Doe")
            .birthDate(LocalDate.of(1990, 5, 20))
            .monthlyIncome(income)
            .requestedAmount(amount)
            .installments(installments)
            .creditScore(700)
            .recentQueries(2)
            .build();
    }

    @Test
    void should_approve_when_commitment_is_below_30_percent() {
        // parcela = 6000/12 = 500 | taxa = 500/5000 = 10%
        CreditRequest request = buildRequest(
            new BigDecimal("5000.00"),
            new BigDecimal("6000.00"),
            12
        );

        assertTrue(incomeCommitmentRule.isSatisfiedBy(request));
    }

    @Test
    void should_approve_when_commitment_is_exactly_30_percent() {
        // parcela = 3000/10 = 300 | taxa = 300/1000 = 30%
        CreditRequest request = buildRequest(
            new BigDecimal("1000.00"),
            new BigDecimal("3000.00"),
            10
        );

        assertTrue(incomeCommitmentRule.isSatisfiedBy(request));
    }

    @Test
    void should_deny_when_commitment_exceeds_30_percent() {
        // parcela = 10000/12 = 833.33 | taxa = 833.33/2000 = 41%
        CreditRequest request = buildRequest(
            new BigDecimal("2000.00"),
            new BigDecimal("10000.00"),
            12
        );

        assertFalse(incomeCommitmentRule.isSatisfiedBy(request));
    }

    @Test
    void should_return_correct_failure_message() {
        assertEquals("Installment exceeds 30% of monthly income.",
            incomeCommitmentRule.getFailureMessage());
    }
}