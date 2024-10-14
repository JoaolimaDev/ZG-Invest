package com.desafio_zg.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio_zg.domain.model.InstrumentQuote;
import com.desafio_zg.domain.model.UserTrade;
import com.desafio_zg.domain.repository.InstrumentQuoteRepository;
import com.desafio_zg.domain.repository.UserTradeRepository;
import com.desafio_zg.dto.DailyReturnDTO;
import com.desafio_zg.dto.HoldingDTO;



@Service
public class TradeService {

    @Autowired
    private UserTradeRepository userTradeRepository;

    @Autowired
    private InstrumentQuoteRepository instrumentQuoteRepository;

    @Transactional(readOnly = true)
    public List<?>  calculateReturnsOverRange(LocalDate startDate, LocalDate endDate) {

        Map<String, HoldingDTO> holdings = new HashMap<>();

        List<?> result = List.of(holdings);

        List<UserTrade> userTrades = userTradeRepository.findBetweendataList(startDate, endDate);

        List<String> listSymbols = userTrades.stream().map(trade -> trade.getInstrument())
        .distinct().collect(Collectors.toList()); 
        
        List<InstrumentQuote> instrumentQuotes = instrumentQuoteRepository.findBetweenDate(startDate, endDate, listSymbols);

        List<LocalDate> tradesByday = userTrades.stream().map(mapper -> mapper.getData())
        .distinct().collect(Collectors.toList()); 


        ListIterator<String> simbolsList = listSymbols.listIterator();
        String simbols = "";
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
           

            if (tradesByday.contains(date)) {
                int quantidadeAtual = 0;
             
                if (simbolsList.hasNext()) {
                    simbols = simbolsList.next();
                }
              
                for (UserTrade trades : userTrades) {

                
                    if (simbols.equals(trades.getInstrument())) {

                        //int quantidadeAtual = 0;
                        String key = trades.getData().toString();
                        String transacoesKeys = trades.getInstrument();

                        HoldingDTO tradesIsntrumentData = new HoldingDTO();
                        DailyReturnDTO dailyReturnDTO = new DailyReturnDTO();
                        HashMap<String, DailyReturnDTO> pass = new HashMap();


                        if (!holdings.containsKey(key)) {
                            pass.put(trades.getInstrument(), dailyReturnDTO);
                            tradesIsntrumentData.setTransacoes(pass);
                            
                            holdings.putIfAbsent(key, tradesIsntrumentData);
                        }

                        if (holdings.containsKey(key)) {

                            Map<String, DailyReturnDTO> transacoes = holdings.get(key).getTransacoes();

                            if (transacoes.containsKey(transacoesKeys)) {
                                
                        
                                quantidadeAtual += trades.getQuantidade(); 
                                DailyReturnDTO retornoDTO = transacoes.get(transacoesKeys);
                                retornoDTO.setQuantity(quantidadeAtual);

                                
                                System.err.println(key + " " + transacoesKeys + " " + trades.getQuantidade() );
                            }else{
                                System.out.println(simbols);
                                quantidadeAtual = trades.getQuantidade();
                                dailyReturnDTO.setQuantity(quantidadeAtual);
                                transacoes.put(transacoesKeys, dailyReturnDTO);
        
                            }
                            
                            // System.out.println(simbols);
                            // quantidadeAtual = trades.getQuantidade();
                            // dailyReturnDTO.setQuantity(quantidadeAtual);
                            // transacoes.put(transacoesKeys, dailyReturnDTO);
        
                            // System.err.println(key + " " + transacoesKeys + " initialized with " + quantidadeAtual);
                            

                        
                        }
                        
                    }
                    
                    //System.err.println(key + " " + trades.getInstrument() + " " + trades.getQuantidade() + " somados" + quantidadeAtual);
                }
                    
                   
                    
                }
            }

        return result;
    }

}
