package com.desafio_zg.dto;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor
    public class ResultadoCarteiraDTO {
        
        private List<String> ativo; 
        private LocalDate dataReferencia;
        private Double saldoAtual;
        private Double custoTotal;
        private Double rendimento;
        private int totalAcoes;
        private int quantidadeVendida;
        private Double valorTotalVendas;
        private Boolean isTransacao;
        private Boolean isVenda;

        public String getRendimentoFormatado() {
            return String.format("%.2f%%", rendimento);
        }


    }
