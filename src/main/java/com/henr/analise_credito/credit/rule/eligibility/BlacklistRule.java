package com.henr.analise_credito.credit.rule.eligibility;

import java.util.Random;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.henr.analise_credito.credit.model.CreditRequest;
import com.henr.analise_credito.credit.rule.CreditRule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(2)
public class BlacklistRule implements CreditRule {

    private static final double BLACKLIST_PROBABILITY = 0.20; // 20%
    private final Random random = new Random();

    @Override
    public boolean isSatisfiedBy(CreditRequest request) {
        log.debug("Evaluating blacklist rule for CPF: {}", request.getCpf());

        boolean isBlacklisted = random.nextDouble() < BLACKLIST_PROBABILITY;

        if (isBlacklisted) {
            log.warn("Denied: CPF {} found in blacklist (simulated)", request.getCpf());
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