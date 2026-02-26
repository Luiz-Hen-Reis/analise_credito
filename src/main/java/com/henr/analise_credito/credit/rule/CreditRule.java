package com.henr.analise_credito.credit.rule;

import com.henr.analise_credito.credit.model.CreditRequest;

public interface CreditRule {
  boolean isSatisfiedBy(CreditRequest request);
  String getFailureMessage();
}
