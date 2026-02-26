package com.henr.analise_credito.credit.rule.eligibility;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.henr.analise_credito.credit.model.CreditRequest;
import com.henr.analise_credito.credit.rule.CreditRule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(1)
public class AgeRule implements CreditRule {
  
  private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 75;

    @Override
    public boolean isSatisfiedBy(CreditRequest request) {
      int age = Period.between(request.getBirthDate(), LocalDate.now()).getYears();
      log.debug("Evaluating age rule for CPF: {} - Age: {}", request.getCpf(), age);

      if (age < MIN_AGE || age > MAX_AGE) {
          log.warn("Denied: Age {} is outside allowed range [{}, {}]", age, MIN_AGE, MAX_AGE);
          return false;
      }

      log.info("Age rule passed for CPF: {}", request.getCpf());
      return true;
    }

    @Override
    public String getFailureMessage() {
        return "Customer age must be between 18 and 75 years.";
    }
}
