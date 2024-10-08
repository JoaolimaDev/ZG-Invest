package com.desafio_zg.service;

import java.util.HashMap;

import com.desafio_zg.dto.Request;

public interface BolsaService {

    HashMap<Object, Object> calcularRendimentos(Request request);
    
}
