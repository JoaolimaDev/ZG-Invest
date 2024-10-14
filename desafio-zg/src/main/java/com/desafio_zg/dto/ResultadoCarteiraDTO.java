package com.desafio_zg.dto;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultadoCarteiraDTO {
    
    private String ativo;
    private LocalDate dataReferencia;
    private Double saldoAtual;
    private Double rendimento;
    private int totalAcoes;


    public String getRendimentoFormatado() {
        return String.format("%.2f%%", rendimento);
    }

}

