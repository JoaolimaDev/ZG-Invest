package com.desafio_zg.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.desafio_zg.config.ExceptionHandler.CustomException;
import com.desafio_zg.domain.model.UserTrade;
import com.desafio_zg.domain.repository.UserTradeRepository;
import com.desafio_zg.dto.Request;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BolsaServiceImpl implements BolsaService {

    public final UserTradeRepository userTradeRepository;

    @Override
    public HashMap<Object, Object> calcularRendimentos(Request request) {

        Date date = request.date();

        List<UserTrade> transacoes = userTradeRepository.findBydate(date);

        if (transacoes.isEmpty()) {
            
            throw new CustomException(String.format("Não foi encontrado nenhum registro para a data enviada: %s"
            , date.toString()), HttpStatus.BAD_REQUEST);
        }

        double custoTotal = transacoes.stream()
        .filter(predicate -> predicate.getTipoOperacao().equals("C"))
        .mapToDouble(mapper -> mapper.getQuantidade().doubleValue() * mapper.getPreco().doubleValue()).sum();

        double valorTotalVendas = transacoes.stream()
        .filter(predicate -> predicate.getTipoOperacao().equals("V"))
        .mapToDouble(mapper -> mapper.getQuantidade().doubleValue() * mapper.getPreco().doubleValue()).sum();
        

        int quantidadeTotal = transacoes.stream()
        .mapToInt(mapper -> mapper.getTipoOperacao().equals("C") ? 
        - mapper.getQuantidade().intValue() : mapper.getQuantidade().intValue()).sum();

        return null;
    }
    
}
