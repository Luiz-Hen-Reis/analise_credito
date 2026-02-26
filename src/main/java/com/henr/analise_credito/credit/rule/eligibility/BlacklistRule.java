package com.henr.analise_credito.credit.rule.eligibility;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.henr.analise_credito.credit.model.CreditRequest;
import com.henr.analise_credito.credit.rule.CreditRule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(2)
public class BlacklistRule implements CreditRule {

    @Override
    public boolean isSatisfiedBy(CreditRequest request) {
        log.debug("Evaluating blacklist rule for CPF: {}", request.getCpf());

        if (Boolean.TRUE.equals(request.getIsBlacklisted())) {
            log.warn("Denied: CPF {} is blacklisted", request.getCpf());
            return false;
        }

        log.info("Blacklist rule passed for CPF: {}", request.getCpf());
        return true;
    }

    @Override
    public String getFailureMessage() {
        return "Customer is blacklisted due to previous defaults.";
    }
}