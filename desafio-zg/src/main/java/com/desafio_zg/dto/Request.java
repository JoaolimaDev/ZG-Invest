package com.desafio_zg.dto;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Request(
    
   @NotNull @NotBlank(message="O campo data é obrigatório!") Date date

){}