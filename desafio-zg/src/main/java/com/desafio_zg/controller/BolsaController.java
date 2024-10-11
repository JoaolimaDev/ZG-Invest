package com.desafio_zg.controller;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desafio_zg.service.BolsaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bolsa-controller")
@RequiredArgsConstructor
public class BolsaController {

    public final BolsaService bolsaService;

    @GetMapping("/calcularRendimentos")
    public ResponseEntity<List<?>> calcularRendimentos(@RequestParam String request) throws ParseException{


        LocalDate dataConsulta = LocalDate.parse(request, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<?> response = bolsaService.calcularRendimentos(dataConsulta);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
    
}
