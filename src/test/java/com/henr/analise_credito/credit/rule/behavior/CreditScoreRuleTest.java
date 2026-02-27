package com.henr.analise_credito.credit.rule.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.henr.analise_credito.credit.model.CreditRequest;

public class CreditScoreRuleTest {

    private CreditScoreRule creditScoreRule;

    @BeforeEach
    void setup() {
        creditScoreRule = new CreditScoreRule();
    }

    private CreditRequest buildRequestWithScore(int score) {
        return CreditRequest.builder()
            .cpf("12345678901")
            .name("John Doe")
            .birthDate(LocalDate.of(1990, 5, 20))
            .monthlyIncome(new BigDecimal("3000.00"))
            .requestedAmount(new BigDecimal("10000.00"))
            .installments(12)
            .creditScore(score)
            .recentQueries(2)
            .build();
    }

    @Test
    void should_approve_when_score_is_exactly_minimum() {
        assertTrue(creditScoreRule.isSatisfiedBy(buildRequestWithScore(600)));
    }

    @Test
    void should_approve_when_score_is_above_minimum() {
        assertTrue(creditScoreRule.isSatisfiedBy(buildRequestWithScore(800)));
    }

    @Test
    void should_deny_when_score_is_below_minimum() {
        assertFalse(creditScoreRule.isSatisfiedBy(buildRequestWithScore(599)));
    }

    @Test
    void should_deny_when_score_is_zero() {
        assertFalse(creditScoreRule.isSatisfiedBy(buildRequestWithScore(0)));
    }

    @Test
    void should_return_correct_failure_message() {
        assertEquals("Credit score is below the minimum required score of 600.",
            creditScoreRule.getFailureMessage());
    }
}