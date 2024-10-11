package com.desafio_zg.dto;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultadoCarteiraDTO {
    
    private LocalDate dataReferencia;
    private Double saldoAtual;
    private Double rendimento;
    private Double totalAcoes;

    public String getRendimentoFormatado() {
        return String.format("%1.2f", rendimento);
    }

}
