package com.henr.analise_credito.credit.rule.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.henr.analise_credito.credit.model.CreditRequest;

public class InstallmentAgeRuleTest {

    private InstallmentAgeRule installmentAgeRule;

    @BeforeEach
    void setup() {
        installmentAgeRule = new InstallmentAgeRule();
    }

    private CreditRequest buildRequest(LocalDate birthDate, int installments) {
        return CreditRequest.builder()
            .cpf("12345678901")
            .name("John Doe")
            .birthDate(birthDate)
            .monthlyIncome(new BigDecimal("3000.00"))
            .requestedAmount(new BigDecimal("10000.00"))
            .installments(installments)
            .creditScore(700)
            .recentQueries(2)
            .build();
    }

    @Test
    void should_approve_when_age_at_end_is_below_maximum() {
        // 35 anos + (24 meses / 12) = 37 anos no fim
        LocalDate birthDate = LocalDate.now().minusYears(35);
        CreditRequest request = buildRequest(birthDate, 24);

        assertTrue(installmentAgeRule.isSatisfiedBy(request));
    }

    @Test
    void should_approve_when_age_at_end_is_exactly_maximum() {
        // 78 anos + (24 meses / 12) = 80 anos no fim
        LocalDate birthDate = LocalDate.now().minusYears(78);
        CreditRequest request = buildRequest(birthDate, 24);

        assertTrue(installmentAgeRule.isSatisfiedBy(request));
    }

    @Test
    void should_deny_when_age_at_end_exceeds_maximum() {
        // 70 anos + (144 meses / 12) = 82 anos no fim
        LocalDate birthDate = LocalDate.now().minusYears(70);
        CreditRequest request = buildRequest(birthDate, 144);

        assertFalse(installmentAgeRule.isSatisfiedBy(request));
    }

    @Test
    void should_deny_when_customer_is_already_above_maximum() {
        // 81 anos + (12 meses / 12) = 82 anos no fim
        LocalDate birthDate = LocalDate.now().minusYears(81);
        CreditRequest request = buildRequest(birthDate, 12);

        assertFalse(installmentAgeRule.isSatisfiedBy(request));
    }

    @Test
    void should_return_correct_failure_message() {
        assertEquals("Customer age at the end of contract exceeds the maximum allowed age of 80.",
            installmentAgeRule.getFailureMessage());
    }
}