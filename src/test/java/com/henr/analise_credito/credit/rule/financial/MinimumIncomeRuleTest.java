package com.henr.analise_credito.credit.rule.financial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.henr.analise_credito.credit.model.CreditRequest;

public class MinimumIncomeRuleTest {

    private MinimumIncomeRule minimumIncomeRule;

    @BeforeEach
    void setup() {
        minimumIncomeRule = new MinimumIncomeRule();
    }

    private CreditRequest buildRequestWithIncome(BigDecimal income) {
        return CreditRequest.builder()
            .cpf("12345678901")
            .name("John Doe")
            .birthDate(LocalDate.of(1990, 5, 20))
            .monthlyIncome(income)
            .requestedAmount(new BigDecimal("10000.00"))
            .installments(12)
            .creditScore(700)
            .recentQueries(2)
            .build();
    }

    @Test
    void should_approve_when_income_is_exactly_minimum() {
        CreditRequest request = buildRequestWithIncome(new BigDecimal("2000.00"));

        assertTrue(minimumIncomeRule.isSatisfiedBy(request));
    }

    @Test
    void should_approve_when_income_is_above_minimum() {
        CreditRequest request = buildRequestWithIncome(new BigDecimal("5000.00"));

        assertTrue(minimumIncomeRule.isSatisfiedBy(request));
    }

    @Test
    void should_deny_when_income_is_below_minimum() {
        CreditRequest request = buildRequestWithIncome(new BigDecimal("1999.99"));

        assertFalse(minimumIncomeRule.isSatisfiedBy(request));
    }

    @Test
    void should_deny_when_income_is_zero() {
        CreditRequest request = buildRequestWithIncome(BigDecimal.ZERO);

        assertFalse(minimumIncomeRule.isSatisfiedBy(request));
    }

    @Test
    void should_return_correct_failure_message() {
        assertEquals("Monthly income is below the minimum required of R$ 2,000.00.",
            minimumIncomeRule.getFailureMessage());
    }
}