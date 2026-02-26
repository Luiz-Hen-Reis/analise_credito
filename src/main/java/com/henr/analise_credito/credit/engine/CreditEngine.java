package com.henr.analise_credito.credit.engine;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.henr.analise_credito.credit.model.AnalysisResult;
import com.henr.analise_credito.credit.model.CreditRequest;
import com.henr.analise_credito.credit.rule.CreditRule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditEngine {
  
  private final List<CreditRule> rules;

  public AnalysisResult process(CreditRequest request) {
    log.info("Starting credit engine for CPF: {} with {} rules", request.getCpf(), rules.size());
    long start = System.currentTimeMillis();

    for (CreditRule rule : rules) {
      if (!rule.isSatisfiedBy(request)) {
        log.error("Analysis DENIED at rule: {}", rule.getClass().getSimpleName());
        return AnalysisResult.deny(rule.getFailureMessage());
      }
    }

    int score = calculateScore(request);
    log.info("Analysis APPROVED for CPF: {} - Score: {} - {}ms",
      request.getCpf(), score, System.currentTimeMillis() - start);

    return AnalysisResult.approve(score);
  }

  private int calculateScore(CreditRequest request) {
    int score = request.getCreditScore();
    if (request.getRecentQueries() == 0) score += 20;
    if (request.getMonthlyIncome().compareTo(new BigDecimal("5000")) > 0) score += 30;
    return Math.min(score, 1000);
  }
}
