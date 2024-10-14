package com.desafio_zg.service;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.desafio_zg.dto.ResultadoCarteiraDTO;



public interface BolsaService {

   Map<String, Map<String, ResultadoCarteiraDTO>> 
   calcularRendimentos(LocalDate datInicio, LocalDate dataFinal,  Pageable pageable);
    
}
