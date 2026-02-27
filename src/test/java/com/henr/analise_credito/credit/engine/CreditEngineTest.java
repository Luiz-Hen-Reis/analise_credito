package com.henr.analise_credito.credit.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.henr.analise_credito.credit.model.AnalysisResult;
import com.henr.analise_credito.credit.model.CreditRequest;
import com.henr.analise_credito.credit.rule.CreditRule;

@ExtendWith(MockitoExtension.class)
public class CreditEngineTest {

    @Mock
    private CreditRule ruleOne;

    @Mock
    private CreditRule ruleTwo;

    @InjectMocks
    private CreditEngine creditEngine;

    private CreditRequest buildRequest(int creditScore, int recentQueries, BigDecimal income) {
        return CreditRequest.builder()
            .cpf("12345678901")
            .name("John Doe")
            .birthDate(LocalDate.of(1990, 5, 20))
            .monthlyIncome(income)
            .requestedAmount(new BigDecimal("10000.00"))
            .installments(12)
            .creditScore(creditScore)
            .recentQueries(recentQueries)
            .build();
    }

    @Test
    void should_approve_when_all_rules_pass() {
        CreditRequest request = buildRequest(700, 2, new BigDecimal("3000.00"));

        when(ruleOne.isSatisfiedBy(request)).thenReturn(true);
        when(ruleTwo.isSatisfiedBy(request)).thenReturn(true);

        // injeta as regras manualmente pois @InjectMocks não injeta List<>
        CreditEngine engine = new CreditEngine(List.of(ruleOne, ruleTwo));

        AnalysisResult result = engine.process(request);

        assertTrue(result.isApproved());
        verify(ruleOne).isSatisfiedBy(request);
        verify(ruleTwo).isSatisfiedBy(request);
    }

    @Test
    void should_deny_on_first_failing_rule_and_skip_remaining() {
        CreditRequest request = buildRequest(700, 2, new BigDecimal("3000.00"));

        when(ruleOne.isSatisfiedBy(request)).thenReturn(false);
        when(ruleOne.getFailureMessage()).thenReturn("Rule one failed");

        CreditEngine engine = new CreditEngine(List.of(ruleOne, ruleTwo));

        AnalysisResult result = engine.process(request);

        assertFalse(result.isApproved());
        assertEquals("Rule one failed", result.getReason());

        // ruleTwo nunca deve ser avaliada
        verify(ruleTwo, never()).isSatisfiedBy(request);
    }

    @Test
    void should_add_bonus_score_when_no_recent_queries() {
        // 0 consultas recentes = +20 pontos
        CreditRequest request = buildRequest(700, 0, new BigDecimal("3000.00"));
        CreditEngine engine = new CreditEngine(List.of());

        AnalysisResult result = engine.process(request);

        assertTrue(result.isApproved());
        assertEquals(720, result.getCalculatedScore()); // 700 + 20
    }

    @Test
    void should_add_bonus_score_when_income_above_5000() {
        // renda > 5000 = +30 pontos
        CreditRequest request = buildRequest(700, 1, new BigDecimal("6000.00"));
        CreditEngine engine = new CreditEngine(List.of());

        AnalysisResult result = engine.process(request);

        assertTrue(result.isApproved());
        assertEquals(730, result.getCalculatedScore()); // 700 + 30
    }

    @Test
    void should_add_both_bonuses_when_conditions_met() {
        // 0 consultas + renda > 5000 = +50 pontos
        CreditRequest request = buildRequest(700, 0, new BigDecimal("6000.00"));
        CreditEngine engine = new CreditEngine(List.of());

        AnalysisResult result = engine.process(request);

        assertTrue(result.isApproved());
        assertEquals(750, result.getCalculatedScore()); // 700 + 20 + 30
    }

    @Test
    void should_cap_score_at_1000() {
        CreditRequest request = buildRequest(990, 0, new BigDecimal("6000.00"));
        CreditEngine engine = new CreditEngine(List.of());

        AnalysisResult result = engine.process(request);

        // 990 + 20 + 30 = 1040, mas deve ser limitado a 1000
        assertEquals(1000, result.getCalculatedScore());
    }
}