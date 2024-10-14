package com.desafio_zg.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.desafio_zg.config.ExceptionHandler.CustomException;
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
    public Map<String, Map<String, ResultadoCarteiraDTO>> calcularRendimentos(LocalDate datInicio,
    LocalDate dataFinal,  Pageable pageable) {

        if (dataFinal.isBefore(datInicio)) {
            throw new CustomException("Error data inicio enviada anterior a data final",
            HttpStatus.BAD_REQUEST);
        }

        Optional<UserTrade> tradeOptional = userTradeRepository.findTopBydataOrderByDataDesc(datInicio);

        if (tradeOptional.get().getData().isAfter(datInicio)) {
            throw new CustomException("Error data inicio enviada maior que o inicio dos investimentos",
            HttpStatus.BAD_REQUEST);
        }

        List<UserTrade> transacoes = userTradeRepository.findBetweendata(datInicio, dataFinal);


        List<String> listSimbols = transacoes.stream().
        map(UserTrade::getInstrument).distinct().collect(Collectors.toList());

        ArrayList<InstrumentQuote> fechamento = instrumentRepository
        .findBetweenDate(datInicio, dataFinal, listSimbols);

        ListIterator<String> simbolsListIterator = listSimbols.listIterator();
        Map<String, Map<String, ResultadoCarteiraDTO>> resultadoMap = new HashMap<>();
        int lastQuantidadeAtual = 0;
        double lastCustoTotal = 0;
        double saldoAtual = 0;
        double rendimento = 0;
        double lucroRealizado = 0;
        

        while (simbolsListIterator.hasNext()) {
            String simbols = simbolsListIterator.next();
            double custoTotal = 0; 
            double valorTotalVendas = 0; 
            int quantidadeAtual = 0;   
            int quantidadeVendida = 0;     

            for (UserTrade transacao : transacoes) {
                if (transacao.getInstrument().equals(simbols)) {
                    LocalDate transacaoDate = transacao.getData();
                    String dateString = transacaoDate.toString();
    
                    Map<String, ResultadoCarteiraDTO> dateMap = resultadoMap.computeIfAbsent(dateString, key -> new HashMap<>());
        
                    ResultadoCarteiraDTO resultado = dateMap.computeIfAbsent(simbols, symbolKey -> {
                        ResultadoCarteiraDTO newResult = new ResultadoCarteiraDTO();
                        newResult.setDataReferencia(transacaoDate);
                        newResult.setAtivo(new ArrayList<>());
                        return newResult;
                    });
        
             
                    if (transacao.getTipoOperacao().equals("C")) {
                        quantidadeAtual += transacao.getQuantidade();
                        custoTotal += transacao.getValorTotal();
                        resultado.setIsVenda(false);
                    }else if (transacao.getTipoOperacao().equals("V")) {
                        quantidadeAtual -= transacao.getQuantidade();
                        quantidadeVendida += transacao.getQuantidade();
                        valorTotalVendas += transacao.getValorTotal();


                        resultado.setValorTotalVendas(valorTotalVendas);
                        resultado.setQuantidadeVendida(quantidadeVendida);
                        resultado.setIsVenda(true);

                    }

                    resultado.setTotalAcoes(quantidadeAtual);
                    resultado.setCustoTotal(custoTotal);
                    resultado.setIsTransacao(true);
                    


                    if (!resultado.getAtivo().contains(simbols)) {
                        resultado.getAtivo().add(simbols);
                    }
                }

            }

            for (InstrumentQuote fechamentos : fechamento) {

                if (fechamentos.getSimbol().equalsIgnoreCase(simbols)) {
                    LocalDate fechamentoDate = fechamentos.getDate();
                    String fechamentoDateString = fechamentoDate.toString();
                
                

                    if (resultadoMap.containsKey(fechamentoDateString)) {
                        Map<String, ResultadoCarteiraDTO> previousDateMap = resultadoMap.get(fechamentoDateString);
                        ResultadoCarteiraDTO previousResult = previousDateMap.get(simbols);

                      
                        if (previousResult != null && previousResult.getIsTransacao()) {

                            if (!previousResult.getIsVenda()) {
                             
                                lastQuantidadeAtual = previousResult.getTotalAcoes();
                                lastCustoTotal = previousResult.getCustoTotal();
    
                                saldoAtual = lastQuantidadeAtual * fechamentos.getPrice();
                                rendimento = ((saldoAtual - lastCustoTotal) / lastCustoTotal) * 100; 
    
                                previousResult.setRendimento(rendimento);
                                previousResult.setSaldoAtual(saldoAtual);
                            
                                //System.out.println(previousResult.getAtivo() + " " +previousResult.getCustoTotal() + "compra");
                            
                            }else if(previousResult.getIsVenda()){
                                
                                lucroRealizado = previousResult.getValorTotalVendas() - previousResult.getCustoTotal();
                                rendimento = (lucroRealizado / custoTotal) * 100;
                                previousResult.setRendimento(rendimento);

                                //System.out.println(previousResult.getAtivo() + " " +previousResult.getValorTotalVendas());

                            }
                        }

                    

                    }

                 
                    Map<String, ResultadoCarteiraDTO> dateMap = resultadoMap.computeIfAbsent(fechamentoDateString, key -> new HashMap<>());

                    ResultadoCarteiraDTO resultado = dateMap.computeIfAbsent(fechamentos.getSimbol(), symbolKey -> {
                        ResultadoCarteiraDTO newResult = new ResultadoCarteiraDTO();
                        newResult.setDataReferencia(fechamentoDate);
                        newResult.setAtivo(new ArrayList<>());
                        return newResult;
                    });

                    resultado.setIsTransacao(false);

                    if (!resultado.getAtivo().contains(fechamentos.getSimbol())) {
                        resultado.getAtivo().add(fechamentos.getSimbol());
                    }
                    
                
               
                }
            }
            
        
        }
        
        return resultadoMap;
        
    
    }
}
