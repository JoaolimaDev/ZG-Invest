package com.desafio_zg.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DailyReturnDTO {
    private double quantity;
    private double cost;
    private boolean isTransacao;
    private String dataref;

}
