package com.desafio_zg.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.Set;
import java.util.HashSet;
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
        DailyReturnDTO holdingDTO02 = new DailyReturnDTO();    
        Map<String, HoldingDTO> holdings = new HashMap<>();
        Map<String, HoldingDTO> holdingsTeste = new HashMap<>();
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
        HoldingDTO transacoesCapture = new HoldingDTO();
            Map<String, DailyReturnDTO> lastTransacoes = new HashMap<>();
        double quantiy = 0;
        String cutOff = "2078-01-12";
        Set<String> set = new HashSet<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDate currentDate = date;
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
                HoldingDTO holdingDTO = new HoldingDTO();     
               
                if (!holdings.containsKey(key)) {

                    if (instrumentQuote.getDate().equals(date)) {
                        HoldingDTO tradesIsntrumentData = new HoldingDTO();
                        HashMap<String, DailyReturnDTO> pass = new HashMap();
    
                        dailyReturnDTO.setTransacao(false);
                        pass.put(instrumentQuoteKey, dailyReturnDTO);
                        tradesIsntrumentData.setTransacoes(pass);
                        
                        holdings.putIfAbsent(key, tradesIsntrumentData);
                    }
                    
                }
            

            }
            
            for (InstrumentQuote instrumentQuote02 : instrumentQuotes) {


                String key02 = date.toString();
                String instrumentQuoteKey02 = instrumentQuote02.getSimbol();
               
    
                if (holdings.containsKey(key02)) {
                    
                    Map<String, DailyReturnDTO> transacoes = holdings.get(key02).getTransacoes();
                    DailyReturnDTO transicaoFound = transacoes.get(instrumentQuoteKey02);

                    if (transacoes.containsKey(instrumentQuoteKey02)) {

                        if(key02.equals(instrumentQuote02.getDate().toString())){

                            if (transicaoFound.isTransacao()) {             
                               
                                cutOff = date.toString();
                                double newQuantity = transicaoFound.getQuantity();
                                quantiy = transicaoFound.getQuantity();

                                lastTransacoes.computeIfAbsent(instrumentQuoteKey02, k -> transicaoFound);

                                lastTransacoes.compute(instrumentQuoteKey02, (k, existingTransaction) -> {
                                    double existingQuantity = existingTransaction.getQuantity();
                                    double differenceThreshold = 0.01; // 1% 
            
                                    if (Math.abs(newQuantity - existingQuantity) > existingQuantity * differenceThreshold) {
                                       
                                        return transicaoFound; 
                                    } else {

                                        return existingTransaction;
                                    }
                                });
  

                            }

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate datecuteDateOff = LocalDate.parse(cutOff, formatter);

                            if (date.isAfter(datecuteDateOff)) {
                                
                        
                                lastTransacoes.forEach((key, dailyReturnDTO) -> {

                                   
                                    HoldingDTO holdingDTONew = holdings.getOrDefault(key02, new HoldingDTO());
                                    Map<String, DailyReturnDTO> transacaoNew = holdingDTONew.getTransacoes();
                                    if (transacaoNew == null) {
                                        transacaoNew = new HashMap<>();
                                    }
                                
                                    DailyReturnDTO dailyReturnDTONew = new DailyReturnDTO(); 
                                    dailyReturnDTONew.setQuantity(dailyReturnDTO.getQuantity());

                                    transacaoNew.put(key, dailyReturnDTONew);
                                    holdingDTONew.setTransacoes(transacaoNew);
                                    holdings.put(key02, holdingDTONew);
                                
                                    System.out.println(key02 + " " + holdingDTONew);
                                    
                                });

                                break;

                            }
                        }                  
                    }

                }

              

            }

        }
    
      

        return result;
    }

}
