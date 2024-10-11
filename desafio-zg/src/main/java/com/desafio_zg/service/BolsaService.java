package com.desafio_zg.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.desafio_zg.dto.ResultadoCarteiraDTO;



public interface BolsaService {

   Page<ResultadoCarteiraDTO>  calcularRendimentos(LocalDate request,  Pageable pageable);
    
}
