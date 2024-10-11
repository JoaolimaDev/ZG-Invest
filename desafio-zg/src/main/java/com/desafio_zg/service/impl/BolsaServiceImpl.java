package com.desafio_zg.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.desafio_zg.domain.model.InstrumentQuote;
import com.desafio_zg.domain.model.UserTrade;
import com.desafio_zg.domain.repository.InstrumentQuoteRepository;
import com.desafio_zg.domain.repository.UserTradeRepository;
import com.desafio_zg.dto.ResultadoCarteiraDTO;
import com.desafio_zg.service.BolsaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BolsaServiceImpl implements BolsaService {

    public final UserTradeRepository userTradeRepository;
    public final InstrumentQuoteRepository instrumentRepository;

    @Override
    public  List<?>  calcularRendimentos(LocalDate request) {

        LocalDate data = request;
        List<UserTrade> transacoes = userTradeRepository.findBydataLessThanEqual(data);

        List<String> listSimbols = transacoes.stream().
        map(UserTrade::getInstrument).distinct().collect(Collectors.toList());

        List<InstrumentQuote> fechamento = instrumentRepository
        .findByDateLessThanEqualAndSimbolIn(data, listSimbols);

        int idx = 0;
        int quantidadeAtual = 0;
        double custoTotal = 0;
        BigDecimal saldoAtual = BigDecimal.ZERO;
        BigDecimal rendimento = BigDecimal.ZERO;

        List<ResultadoCarteiraDTO> resultadoCarteiraList = new ArrayList<>(); 
        
        for (InstrumentQuote fechamentos : fechamento) {

            UserTrade transacao = transacoes.get(idx);
            
            quantidadeAtual += transacao.getQuantidade();
            custoTotal += transacao.getValorTotal();
            double precoAtual = fechamentos.getPrice();

            saldoAtual = BigDecimal.valueOf(quantidadeAtual).multiply(BigDecimal.valueOf(precoAtual));
           
            rendimento = saldoAtual.subtract(BigDecimal.valueOf(custoTotal))
            .divide(BigDecimal.valueOf(custoTotal)).multiply(BigDecimal.valueOf(100));

            // System.out.printf("%s, %d ações\n", data, quantidadeAtual);
            // System.out.printf("Saldo atual: R$ %.2f\n", saldoAtual);
  


            idx++;  
            
        }


       
        return resultadoCarteiraList;
    }
    
}
