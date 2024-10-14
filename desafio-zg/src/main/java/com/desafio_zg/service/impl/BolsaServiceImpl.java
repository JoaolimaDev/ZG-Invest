package com.desafio_zg.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public  Page<ResultadoCarteiraDTO>  calcularRendimentos(LocalDate request,  Pageable pageable) {

        LocalDate data = request;
        List<UserTrade> transacoes = userTradeRepository.findBydataLessThanEqual(data, pageable);


        List<String> listSimbols = transacoes.stream().
        map(UserTrade::getEspecificacao).distinct().collect(Collectors.toList());

        List<InstrumentQuote> fechamento = instrumentRepository
        .findByDateLessThanEqualAndSimbolIn(data, listSimbols, pageable);

        int quantidadeAtual = 0;
        double saldoAtual =  0D;
        double rendimento = 0D;
        double custoTotal = 0D;

        List<ResultadoCarteiraDTO> resultadoCarteiraList = new ArrayList<>(); 
        
        for (InstrumentQuote fechamentos : fechamento) {

         
            double precoAtual = fechamentos.getPrice();
            ResultadoCarteiraDTO resultado = new ResultadoCarteiraDTO();
            ListIterator<UserTrade> iterator = transacoes.listIterator();
            
            
            if (iterator.hasNext()) {

                UserTrade transacao = iterator.next();

                if(transacao.getData().isEqual(fechamentos.getDate()) || transacao.getData().isBefore(fechamentos.getDate())){

                    if (transacao.getTipoOperacao().equals("C")) {
                        quantidadeAtual += transacao.getQuantidade();
                        custoTotal += transacao.getValorTotal();
                    } else if (transacao.getTipoOperacao().equals("V")) {
                        quantidadeAtual -= transacao.getQuantidade();
                        custoTotal -= transacao.getValorTotal();
                    }

                }


                iterator.remove();
               
            }


            saldoAtual = quantidadeAtual * precoAtual;
            rendimento = ((saldoAtual - custoTotal) / custoTotal) * 100; 

            resultado.setDataReferencia(fechamentos.getDate());
            resultado.setAtivo(fechamentos.getSimbol());
            resultado.setSaldoAtual(saldoAtual);
            resultado.setTotalAcoes(quantidadeAtual);
            resultado.setRendimento(rendimento);

            resultadoCarteiraList.add(resultado);
            

        }

        return new PageImpl<>(resultadoCarteiraList, pageable, resultadoCarteiraList.size());
    }

    
}

