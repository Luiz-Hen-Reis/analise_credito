package com.henr.analise_credito.credit.rule.financial;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.henr.analise_credito.credit.model.CreditRequest;
import com.henr.analise_credito.credit.rule.CreditRule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(4)
public class IncomeCommitmentRule implements CreditRule {

    private static final BigDecimal MAX_COMMITMENT = new BigDecimal("0.30");

    @Override
    public boolean isSatisfiedBy(CreditRequest request) {
        BigDecimal installmentValue = request.getRequestedAmount()
            .divide(new BigDecimal(request.getInstallments()), 2, RoundingMode.HALF_UP);

        BigDecimal commitmentRate = installmentValue
            .divide(request.getMonthlyIncome(), 4, RoundingMode.HALF_UP);

        log.debug("Income commitment rate for CPF {}: {}%",
            request.getCpf(), commitmentRate.multiply(BigDecimal.valueOf(100)));

        if (commitmentRate.compareTo(MAX_COMMITMENT) > 0) {
            log.warn("Denied: Commitment rate {}% exceeds 30%",
                commitmentRate.multiply(BigDecimal.valueOf(100)));
            return false;
        }

        log.info("Income commitment rule passed for CPF: {}", request.getCpf());
        return true;
    }

    @Override
    public String getFailureMessage() {
        return "Installment exceeds 30% of monthly income.";
    }
}