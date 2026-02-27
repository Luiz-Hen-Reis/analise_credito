package com.henr.analise_credito.credit.rule.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.henr.analise_credito.credit.model.CreditRequest;

public class RecentQueriesRuleTest {

    private RecentQueriesRule recentQueriesRule;

    @BeforeEach
    void setup() {
        recentQueriesRule = new RecentQueriesRule();
    }

    private CreditRequest buildRequestWithQueries(int queries) {
        return CreditRequest.builder()
            .cpf("12345678901")
            .name("John Doe")
            .birthDate(LocalDate.of(1990, 5, 20))
            .monthlyIncome(new BigDecimal("3000.00"))
            .requestedAmount(new BigDecimal("10000.00"))
            .installments(12)
            .creditScore(700)
            .recentQueries(queries)
            .build();
    }

    @Test
    void should_approve_when_queries_is_zero() {
        assertTrue(recentQueriesRule.isSatisfiedBy(buildRequestWithQueries(0)));
    }

    @Test
    void should_approve_when_queries_is_exactly_maximum() {
        assertTrue(recentQueriesRule.isSatisfiedBy(buildRequestWithQueries(5)));
    }

    @Test
    void should_approve_when_queries_is_below_maximum() {
        assertTrue(recentQueriesRule.isSatisfiedBy(buildRequestWithQueries(3)));
    }

    @Test
    void should_deny_when_queries_exceeds_maximum() {
        assertFalse(recentQueriesRule.isSatisfiedBy(buildRequestWithQueries(6)));
    }

    @Test
    void should_return_correct_failure_message() {
        assertEquals("Too many credit queries in the last 30 days.",
            recentQueriesRule.getFailureMessage());
    }
}