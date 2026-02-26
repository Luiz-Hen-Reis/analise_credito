package com.henr.analise_credito.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.henr.analise_credito.dto.CreditAnalysisRequestDTO;
import com.henr.analise_credito.dto.CreditAnalysisResponseDTO;
import com.henr.analise_credito.exception.ErrorResponse;
import com.henr.analise_credito.useCase.RunCreditAnalysisUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/credit-analysis")
@Tag(name = "Credit Analysis", description = "Credit analysis management")
@RequiredArgsConstructor
public class CreditAnalysisController {

    private final RunCreditAnalysisUseCase runCreditAnalysisUseCase;

    @PostMapping
    @Operation(
        summary = "Run credit analysis for a customer",
        responses = {
            @ApiResponse(responseCode = "201", description = "Analysis completed",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreditAnalysisResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<CreditAnalysisResponseDTO> runAnalysis(
            @RequestBody @Valid CreditAnalysisRequestDTO dto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(runCreditAnalysisUseCase.execute(dto));
    }
}