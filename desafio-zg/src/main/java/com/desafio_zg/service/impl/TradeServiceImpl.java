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
import com.desafio_zg.service.TradeService;


@Service
public class TradeServiceImpl implements TradeService {

    @Autowired
    private UserTradeRepository userTradeRepository;

    @Autowired
    private InstrumentQuoteRepository instrumentQuoteRepository;

    @Transactional(readOnly = true)
    @Override
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

        HoldingDTO transacoesCapture = new HoldingDTO();
        Map<String, DailyReturnDTO> lastTransacoes = new HashMap<>();


        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

            if (tradesByday.contains(date)) {
                int quantidadeAtual = 0;
                double custoTotal = 0;
                double quantidadeVendida = 0;   
                double valorTotalComprado = 0;     
                double valorTotalVendas = 0; 


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
                                    valorTotalComprado += trades.getQuantidade();
                                    custoTotal += trades.getValorTotal();
                                    dailyReturnDTO.setVenda(false);
                                }else if (trades.getTipoOperacao().equalsIgnoreCase("V")) {
                                    quantidadeAtual -= trades.getQuantidade(); 
                                    valorTotalVendas += trades.getValorTotal();
                                    quantidadeVendida += trades.getQuantidade();
                                    dailyReturnDTO.setVenda(true);
                                 
                                }

                              
                                DailyReturnDTO retornoDTO = transacoes.get(transacoesKeys);
                                retornoDTO.setQuantity(quantidadeAtual);
                                dailyReturnDTO.setTransacao(true);
                                retornoDTO.setCost(custoTotal);
                                retornoDTO.setValorTotalVendas(valorTotalVendas);
                                retornoDTO.setQuantidadeVendida(quantidadeVendida);
                                retornoDTO.setValorTotalComprado(valorTotalComprado);

                            }else{
                               
                                quantidadeAtual = trades.getQuantidade();
                                if (trades.getTipoOperacao().equalsIgnoreCase("C")) {
                                    custoTotal += trades.getValorTotal();
                                    valorTotalComprado += trades.getQuantidade();
                                    dailyReturnDTO.setVenda(false);
                                }else if (trades.getTipoOperacao().equalsIgnoreCase("V")) {
                                    valorTotalVendas += trades.getValorTotal();
                                    dailyReturnDTO.setVenda(true);
                                    quantidadeVendida += trades.getQuantidade();
            
                                }
                               
                                dailyReturnDTO.setQuantity(quantidadeAtual);
                                dailyReturnDTO.setCost(custoTotal);
                                dailyReturnDTO.setTransacao(true);
                                transacoes.put(transacoesKeys, dailyReturnDTO);
                                dailyReturnDTO.setQuantidadeVendida(quantidadeVendida);
                                dailyReturnDTO.setValorTotalVendas(valorTotalVendas);
                                dailyReturnDTO.setValorTotalComprado(valorTotalComprado);
        
                            }

                           
                            

                        }
                        
                    }
                
                }
                    
                   
                    
            }
           
       
            for (InstrumentQuote instrumentQuote : instrumentQuotes) {

                String key = date.toString();
                String instrumentQuoteKey = instrumentQuote.getSimbol();
                DailyReturnDTO dailyReturnDTO = new DailyReturnDTO();   
               
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
                               
                                double newQuantity = transicaoFound.getQuantity();
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
                                          
                            lastTransacoes.forEach((key, dailyReturnDTO) -> {

                                
                                HoldingDTO holdingDTONew = holdings.getOrDefault(key02, new HoldingDTO());
                                Map<String, DailyReturnDTO> transacaoNew = holdingDTONew.getTransacoes();
                                if (transacaoNew == null) {
                                    transacaoNew = new HashMap<>();
                                }else{

                                    if (transacaoNew.containsKey(instrumentQuoteKey02)) {
                                        

                                        if (transacaoNew.get(instrumentQuoteKey02).getQuantity() <= 0.0) {

                                            if (!transacaoNew.get(instrumentQuoteKey02).isVenda()) {
                                                
                                           

                                                transacaoNew.remove(instrumentQuoteKey02);
                                                
                                            }

                                        }

                                    }

                                    
                                }

                                double rendimento = 0;
                                double saldoAtual = 0;
                                DailyReturnDTO dailyReturnDTONew = new DailyReturnDTO(); 

                                dailyReturnDTONew.setQuantity(dailyReturnDTO.getQuantity());
                                dailyReturnDTONew.setCost(dailyReturnDTO.getCost());
                               
                                

                                if (transacaoNew.get(key) != null) {
                                    
                                    if (transacaoNew.get(key).isVenda()) {

                                    
                                        
                                        double lucroRealizado = dailyReturnDTO.getValorTotalVendas() -
                                        dailyReturnDTO.getCost();
                                        rendimento = (lucroRealizado / dailyReturnDTO.getCost()) * 100;

                                        dailyReturnDTONew.setVenda(true);
                                        dailyReturnDTONew.setValorTotalVendas(dailyReturnDTO.getCost());
                                        dailyReturnDTONew.setQuantidadeVendida(dailyReturnDTO.getQuantidadeVendida());
                                        dailyReturnDTONew.setSaldo(saldoAtual);
                                        
                                    }else{

                                        double lastQuantidadeAtual = dailyReturnDTO.getQuantity();
                                        double lastCustoTotal = dailyReturnDTO.getCost();
        
                                        saldoAtual = lastQuantidadeAtual * instrumentQuote02.getPrice();
                                        rendimento = ((saldoAtual - lastCustoTotal) / lastCustoTotal) * 100;

                                        dailyReturnDTONew.setVenda(false);
                                        dailyReturnDTONew.setValorTotalComprado(dailyReturnDTO.getValorTotalComprado());
                                        dailyReturnDTONew.setSaldo(saldoAtual);
                                    }

                              
    
                                }

                              

                                dailyReturnDTONew.setRendimento(rendimento);
                                dailyReturnDTONew.setSaldo(saldoAtual);
                                transacaoNew.put(key, dailyReturnDTONew);

                                // if (dailyReturnDTONew.getQuantity() <= 0 && dailyReturnDTONew.isVenda() == false) {
                                    
                                    
                                //     transacaoNew.remove(key);
                                    
                                // }

                                holdingDTONew.setTransacoes(transacaoNew);
                                holdings.put(key02, holdingDTONew);

                        

                                
                            });

                          
                        }                  
                    }

                }

              

            }

        }
    
      

        return result;
    }



}
