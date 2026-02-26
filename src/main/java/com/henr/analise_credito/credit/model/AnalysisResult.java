package com.henr.analise_credito.credit.model;

import lombok.Getter;

@Getter
public class AnalysisResult {

    private final boolean approved;
    private final String reason;
    private final int calculatedScore;

    private AnalysisResult(boolean approved, String reason, int calculatedScore) {
        this.approved = approved;
        this.reason = reason;
        this.calculatedScore = calculatedScore;
    }

    public static AnalysisResult approve(int score) {
        return new AnalysisResult(true, "All rules passed successfully", score);
    }

    public static AnalysisResult deny(String reason) {
        return new AnalysisResult(false, reason, 0);
    }
}