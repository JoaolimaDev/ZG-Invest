package com.desafio_zg.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.desafio_zg.domain.model.InstrumentQuote;

@Repository
public interface InstrumentQuoteRepository extends JpaRepository<InstrumentQuote, Long> {
    @Query("SELECT q FROM InstrumentQuote q WHERE q.date BETWEEN :startDate AND :endDate AND q.simbol IN :listSymbols ORDER BY q.date ASC")
    List<InstrumentQuote> findBetweenDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("listSymbols") List<String> listSymbols);
    
    

}

    

