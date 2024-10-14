package com.desafio_zg.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HoldingDTO {

    private Map<String, DailyReturnDTO> transacoes = new HashMap<>();
}
