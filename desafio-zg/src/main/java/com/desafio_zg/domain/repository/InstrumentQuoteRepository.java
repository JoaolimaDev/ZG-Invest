package com.desafio_zg.domain.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.desafio_zg.domain.model.InstrumentQuote;

@Repository
public interface InstrumentQuoteRepository extends JpaRepository<InstrumentQuote, Long> {


    @Query(
        "SELECT E FROM InstrumentQuote E " +
        "WHERE E.date >= :startDate AND E.date <= :endDate " +
        "AND E.simbol IN :simbols " +
        "ORDER BY E.date ASC, E.simbol ASC"
    )
    ArrayList<InstrumentQuote> findBetweenDate(@Param("startDate") LocalDate startDate, 
                                                @Param("endDate") LocalDate endDate, 
                                                @Param("simbols") List<String> simbols);

}

    

