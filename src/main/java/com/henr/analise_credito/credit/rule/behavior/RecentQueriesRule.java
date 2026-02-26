package com.henr.analise_credito.credit.rule.behavior;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.henr.analise_credito.credit.model.CreditRequest;
import com.henr.analise_credito.credit.rule.CreditRule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(6)
public class RecentQueriesRule implements CreditRule {

    private static final int MAX_QUERIES = 5;

    @Override
    public boolean isSatisfiedBy(CreditRequest request) {
        log.debug("Evaluating recent queries for CPF: {} - Queries: {}", request.getCpf(), request.getRecentQueries());

        if (request.getRecentQueries() > MAX_QUERIES) {
            log.warn("Denied: {} recent queries exceed maximum of {}", request.getRecentQueries(), MAX_QUERIES);
            return false;
        }

        log.info("Recent queries rule passed for CPF: {}", request.getCpf());
        return true;
    }

    @Override
    public String getFailureMessage() {
        return "Too many credit queries in the last 30 days.";
    }
}