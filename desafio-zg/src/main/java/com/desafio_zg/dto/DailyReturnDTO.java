package com.desafio_zg.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DailyReturnDTO {
    private double quantity;
    private double cost;
    private boolean isTransacao;
    private Double rendimento;
    private boolean isVenda;
    private double quantidadeVendida;
    private double valorTotalVendas;
    private double valorTotalComprado;
    private double saldo;



    public String getRendimentoFormatado() {
        return String.format("%.2f%%", rendimento);
    }


}
