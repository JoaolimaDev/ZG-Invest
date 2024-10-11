package com.desafio_zg.controller;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desafio_zg.dto.ResultadoCarteiraDTO;
import com.desafio_zg.service.BolsaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/zg-invest")
@RequiredArgsConstructor
@Tag(name = "ZG INVEST CONTROLLER", description = "Endpoints relacionados a zgInvestController")
public class zgInvestController {

    public final BolsaService bolsaService;

     @Operation(
        summary = "Calcule os redimentos do portfólio com base na data, retorno dos rendimentos da data enviada e histórico", 
        description = "calcula o retorno da carteira para uma determinada data, com parâmetros de paginação opcionais (página e limite).",
        tags = {"ZG INVEST CONTROLLER"} 
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", 
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = ResultadoCarteiraDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/calcularRendimentos")
    public ResponseEntity<Page<ResultadoCarteiraDTO> > calcularRendimentos(@RequestParam String date,
    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit) throws ParseException{

        // if (date.isEmpty() || date.isBlank()) {
        //     throw new CustomException("O campo data é obrigatório!", HttpStatus.BAD_REQUEST);
        // }

        LocalDate dataConsulta = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Page<ResultadoCarteiraDTO> response = bolsaService.calcularRendimentos(dataConsulta, PageRequest.of(page, limit));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
}
