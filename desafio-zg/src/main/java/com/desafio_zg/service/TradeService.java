package com.desafio_zg.service;

import java.time.LocalDate;

import java.util.*;


public interface TradeService {

   List<?>  calculateReturnsOverRange(LocalDate startDate, LocalDate endDate);
    
}
