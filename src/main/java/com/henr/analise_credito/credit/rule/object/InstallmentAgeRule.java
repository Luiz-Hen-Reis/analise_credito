package com.henr.analise_credito.credit.rule.object;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.henr.analise_credito.credit.model.CreditRequest;
import com.henr.analise_credito.credit.rule.CreditRule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(7)
public class InstallmentAgeRule implements CreditRule {

    private static final int MAX_AGE_AT_END = 80;

    @Override
    public boolean isSatisfiedBy(CreditRequest request) {
        int currentAge = Period.between(request.getBirthDate(), LocalDate.now()).getYears();
        int ageAtEnd = currentAge + (request.getInstallments() / 12);

        log.debug("Age at end of contract for CPF {}: {}", request.getCpf(), ageAtEnd);

        if (ageAtEnd > MAX_AGE_AT_END) {
            log.warn("Denied: Customer will be {} years old at end of contract", ageAtEnd);
            return false;
        }

        log.info("Installment/age rule passed for CPF: {}", request.getCpf());
        return true;
    }

    @Override
    public String getFailureMessage() {
        return "Customer age at the end of contract exceeds the maximum allowed age of 80.";
    }
}