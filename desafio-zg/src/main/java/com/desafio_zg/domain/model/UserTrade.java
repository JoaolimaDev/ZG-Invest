package com.desafio_zg.domain.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_trade")
@Data
public class UserTrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    @Column(name = "tipo_operacao")
    private String tipoOperacao;

    private String mercado;
    private String prazo;
    private String instrument;
    private String especificacao;

    private Double quantidade;
    private Double preco;
    private Double valorTotal;
    
}
