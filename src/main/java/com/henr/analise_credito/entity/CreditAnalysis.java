package com.henr.analise_credito.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "credit_analysis")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreditAnalysis {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false) 
  private Customer customer;

  @Column(name = "calculated_score", nullable = false)
  private Integer calculatedScore;

  @Column(name = "reported_income", nullable = false)
  private BigDecimal reportedIncome;

  @Column(nullable = false)
  private String result;

  @Column(name = "detailed_reason", columnDefinition = "TEXT")
  private String detailedReason;

  @CreationTimestamp
  @Column(name = "processed_at", nullable = false, updatable = false)
  private LocalDateTime processedAt;
}
