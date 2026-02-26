package com.henr.analise_credito.credit.rule.behavior;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.henr.analise_credito.credit.model.CreditRequest;
import com.henr.analise_credito.credit.rule.CreditRule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(5)
public class CreditScoreRule implements CreditRule {

    private static final int MIN_SCORE = 600;

    @Override
    public boolean isSatisfiedBy(CreditRequest request) {
        log.debug("Evaluating credit score for CPF: {} - Score: {}", request.getCpf(), request.getCreditScore());

        if (request.getCreditScore() < MIN_SCORE) {
            log.warn("Denied: Score {} is below minimum {}", request.getCreditScore(), MIN_SCORE);
            return false;
        }

        log.info("Credit score rule passed for CPF: {}", request.getCpf());
        return true;
    }

    @Override
    public String getFailureMessage() {
        return "Credit score is below the minimum required score of 600.";
    }
}