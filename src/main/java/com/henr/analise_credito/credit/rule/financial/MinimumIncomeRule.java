package com.henr.analise_credito.credit.rule.financial;

import java.math.BigDecimal;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.henr.analise_credito.credit.model.CreditRequest;
import com.henr.analise_credito.credit.rule.CreditRule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(3)
public class MinimumIncomeRule implements CreditRule {

    private static final BigDecimal MIN_INCOME = new BigDecimal("2000.00");

    @Override
    public boolean isSatisfiedBy(CreditRequest request) {
        log.debug("Evaluating minimum income for CPF: {}", request.getCpf());

        if (request.getMonthlyIncome().compareTo(MIN_INCOME) < 0) {
            log.warn("Denied: Income {} is below minimum {}", request.getMonthlyIncome(), MIN_INCOME);
            return false;
        }

        log.info("Minimum income rule passed for CPF: {}", request.getCpf());
        return true;
    }

    @Override
    public String getFailureMessage() {
        return "Monthly income is below the minimum required of R$ 2,000.00.";
    }
}