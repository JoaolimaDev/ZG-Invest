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
        double quant = 0;
        String instrumento = "";
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
           
            if (tradesByday.contains(date)) {
                int quantidadeAtual = 0;
                double custoTotal = 0;
             
                if (simbolsList.hasNext()) {
                    simbols = simbolsList.next();
                }
              
                for (UserTrade trades : userTrades) {
                
                    if (simbols.equals(trades.getInstrument())) {

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

                                if (trades.getTipoOperacao().equalsIgnoreCase("C")) {
                                    quantidadeAtual += trades.getQuantidade(); 
                                    custoTotal += trades.getValorTotal();
                                }else if (trades.getTipoOperacao().equalsIgnoreCase("V")) {
                                    quantidadeAtual -= trades.getQuantidade(); 
                                    custoTotal -= trades.getValorTotal();
                                }

                              
                                DailyReturnDTO retornoDTO = transacoes.get(transacoesKeys);
                                retornoDTO.setQuantity(quantidadeAtual);
                                dailyReturnDTO.setTransacao(true);
                                retornoDTO.setCost(custoTotal);

                            }else{
                               
                                quantidadeAtual = trades.getQuantidade();
                                if (trades.getTipoOperacao().equalsIgnoreCase("C")) {
                                    custoTotal += trades.getValorTotal();
                                }else if (trades.getTipoOperacao().equalsIgnoreCase("V")) {
                                    custoTotal -= trades.getValorTotal();
                                }
                               
                                dailyReturnDTO.setQuantity(quantidadeAtual);
                                dailyReturnDTO.setCost(custoTotal);
                                dailyReturnDTO.setTransacao(true);
                                transacoes.put(transacoesKeys, dailyReturnDTO);
        
                            }
                            

                        }
                        
                    }
                
                }
                    
                   
                    
            }

       
            for (InstrumentQuote instrumentQuote : instrumentQuotes) {

                String key = date.toString();
                String instrumentQuoteKey = instrumentQuote.getSimbol();
                DailyReturnDTO dailyReturnDTO = new DailyReturnDTO();
                Map<String, DailyReturnDTO> transacoesCapture = new HashMap<>();  
                HoldingDTO holdingDTO = new HoldingDTO();     
               
                if (!holdings.containsKey(key)) {

                    if (instrumentQuote.getDate().equals(date)) {
                        HoldingDTO tradesIsntrumentData = new HoldingDTO();
                        HashMap<String, DailyReturnDTO> pass = new HashMap();
    
                        dailyReturnDTO.setTransacao(false);
                        // dailyReturnDTO.setQuantity(quantity);
                        pass.put(instrumentQuoteKey, dailyReturnDTO);
                        tradesIsntrumentData.setTransacoes(pass);
                        
                        // System.out.println(key + " " +pass);
                        holdings.putIfAbsent(key, tradesIsntrumentData);
                        // System.err.println(instrumentQuoteKey);
                    }
                    

                }

                if (holdings.containsKey(key)) {
                    
                    Map<String, DailyReturnDTO> transacoes = holdings.get(key).getTransacoes();
                    DailyReturnDTO transicaoFound = transacoes.get(instrumentQuoteKey);

                    if (transacoes.containsKey(instrumentQuoteKey)) {

                        if(key.equals(instrumentQuote.getDate().toString())){

                            if (transicaoFound.isTransacao()) {

                                transacoesCapture.put(instrumentQuoteKey, transicaoFound); 
                                holdingDTO.setTransacoes(transacoesCapture);

                            }
                        }                  
                    }

                    if (!transacoesCapture.isEmpty()) {
                        String nextDateKey = date.plusDays(1).toString();
                        HoldingDTO nextDayHolding = holdings.get(nextDateKey);
                        
                        if (nextDayHolding == null) {
                            holdings.put(nextDateKey, holdingDTO);
                            System.out.println(nextDateKey);
                        }
                    }
             
                
                }
                


    
                // System.out.println(previousdate);
          
                // if (holdings.containsKey(previousdate)) {
                        
                //     System.out.println(previousdate);
                // }

                
                // if (holdings.containsKey(dateKey.toString())) {
                //     Map<String, DailyReturnDTO> previusDTO = holdings.get(dateKey.toString())
                //     .getTransacoes();
                    

                //     if (previusDTO.containsKey(instrumentQuoteKey)) {

                //         if (previusDTO.get(instrumentQuoteKey).getQuantity() > 0) {
                //             quantity = previusDTO.get(instrumentQuoteKey).getQuantity();
                //         }else{
                //             quantity = 0;
                //         }
                //     }
                // }
                
                
                // if (key.equals(instrumentQuote.getDate().toString())) {

                //     HoldingDTO tradesIsntrumentData = new HoldingDTO();
                //     HashMap<String, DailyReturnDTO> pass = new HashMap();

                //     dailyReturnDTO.setTransacao(false);
                //     dailyReturnDTO.setQuantity(quantity);
                //     pass.put(instrumentQuote.getSimbol(), dailyReturnDTO);
                //     tradesIsntrumentData.setTransacoes(pass);
                    
                            
                //     holdings.putIfAbsent(key, tradesIsntrumentData);
                //     System.err.println(key + " " +instrumentQuoteKey);
                // }
                


                // if (holdings.containsKey(key)) {    

                //     Map<String, DailyReturnDTO> transacoes = holdings.get(key).getTransacoes();
                  
                //     if (holdings.containsKey(dateKey.minusDays(1).toString())) {

                //         Map<String, DailyReturnDTO> previusDTO = holdings.get(dateKey.minusDays(1).toString())
                //         .getTransacoes();


                //         if (previusDTO.containsKey(instrumentQuoteKey)) {

                //             if (!transacoes.containsKey(instrumentQuoteKey)) {
                                
                //                 dailyReturnDTO.setTransacao(false);
                //                 // dailyReturnDTO.setQuantity(15.0);
                //                 transacoes.putIfAbsent(instrumentQuoteKey, dailyReturnDTO);

                //                 // System.out.println("Chave atual:" +  dateKey);
                //                 // System.out.println(instrumentQuoteKey);

                //             }
                //         }


                //     }
                    
                // }


            }

        }

        return result;
    }

}
