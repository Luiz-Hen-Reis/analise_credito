package com.henr.analise_credito.credit.rule.eligibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.henr.analise_credito.credit.model.CreditRequest;

public class AgeRuleTest {

    private AgeRule ageRule;

    @BeforeEach
    void setup() {
        ageRule = new AgeRule();
    }

    private CreditRequest buildRequestWithBirthDate(LocalDate birthDate) {
        return CreditRequest.builder()
            .cpf("12345678901")
            .name("John Doe")
            .birthDate(birthDate)
            .monthlyIncome(new BigDecimal("3000.00"))
            .requestedAmount(new BigDecimal("10000.00"))
            .installments(12)
            .creditScore(700)
            .recentQueries(2)
            .build();
    }

    @Test
    void should_approve_when_age_is_exactly_18() {
        CreditRequest request = buildRequestWithBirthDate(LocalDate.now().minusYears(18));

        assertTrue(ageRule.isSatisfiedBy(request));
    }

    @Test
    void should_approve_when_age_is_exactly_75() {
        CreditRequest request = buildRequestWithBirthDate(LocalDate.now().minusYears(75));

        assertTrue(ageRule.isSatisfiedBy(request));
    }

    @Test
    void should_approve_when_age_is_within_range() {
        CreditRequest request = buildRequestWithBirthDate(LocalDate.now().minusYears(35));

        assertTrue(ageRule.isSatisfiedBy(request));
    }

    @Test
    void should_deny_when_age_is_below_18() {
        CreditRequest request = buildRequestWithBirthDate(LocalDate.now().minusYears(17));

        assertFalse(ageRule.isSatisfiedBy(request));
    }

    @Test
    void should_deny_when_age_is_above_75() {
        CreditRequest request = buildRequestWithBirthDate(LocalDate.now().minusYears(76));

        assertFalse(ageRule.isSatisfiedBy(request));
    }

    @Test
    void should_return_correct_failure_message() {
        assertEquals("Customer age must be between 18 and 75 years.", ageRule.getFailureMessage());
    }
}