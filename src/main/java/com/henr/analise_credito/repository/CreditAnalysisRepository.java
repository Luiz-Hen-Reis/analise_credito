package com.henr.analise_credito.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.henr.analise_credito.entity.CreditAnalysis;

public interface CreditAnalysisRepository extends JpaRepository<CreditAnalysis, UUID> {}
