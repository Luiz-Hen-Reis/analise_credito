package com.henr.analise_credito.credit.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreditRequest {
    private String cpf;
    private String name;
    private LocalDate birthDate;
    private BigDecimal monthlyIncome;
    private BigDecimal requestedAmount;
    private Integer installments;
    private Integer creditScore;
    private Integer recentQueries;
    private Boolean isBlacklisted;
}