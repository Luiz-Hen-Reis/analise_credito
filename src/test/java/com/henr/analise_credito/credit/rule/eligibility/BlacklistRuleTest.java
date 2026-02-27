package com.henr.analise_credito.credit.rule.eligibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.henr.analise_credito.credit.model.CreditRequest;

@ExtendWith(MockitoExtension.class)
public class BlacklistRuleTest {

    @Mock
    private Random random;

    private CreditRequest buildRequest() {
        return CreditRequest.builder()
            .cpf("12345678901")
            .name("John Doe")
            .birthDate(LocalDate.of(1990, 5, 20))
            .monthlyIncome(new BigDecimal("3000.00"))
            .requestedAmount(new BigDecimal("10000.00"))
            .installments(12)
            .creditScore(700)
            .recentQueries(2)
            .build();
    }

    @Test
    void should_deny_when_random_is_below_probability() {
        when(random.nextDouble()).thenReturn(0.10); // 10% < 20% = blacklisted
        BlacklistRule rule = new BlacklistRule(random);

        assertFalse(rule.isSatisfiedBy(buildRequest()));
    }

    @Test
    void should_approve_when_random_is_above_probability() {
        when(random.nextDouble()).thenReturn(0.50); // 50% > 20% = not blacklisted
        BlacklistRule rule = new BlacklistRule(random);

        assertTrue(rule.isSatisfiedBy(buildRequest()));
    }

    @Test
    void should_deny_when_random_is_exactly_at_boundary() {
        when(random.nextDouble()).thenReturn(0.199); // logo abaixo de 20%
        BlacklistRule rule = new BlacklistRule(random);

        assertFalse(rule.isSatisfiedBy(buildRequest()));
    }

    @Test
    void should_approve_when_random_is_exactly_at_probability() {
        when(random.nextDouble()).thenReturn(0.20); // exatamente 20% = não blacklisted
        BlacklistRule rule = new BlacklistRule(random);

        assertTrue(rule.isSatisfiedBy(buildRequest()));
    }

    @Test
    void should_return_correct_failure_message() {
        BlacklistRule rule = new BlacklistRule(random);
        assertEquals("Customer is blacklisted due to previous defaults.", rule.getFailureMessage());
    }
}