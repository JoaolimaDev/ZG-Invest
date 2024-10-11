package com.desafio_zg.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.desafio_zg.domain.model.InstrumentQuote;

@Repository
public interface InstrumentQuoteRepository extends JpaRepository<InstrumentQuote, Long> {

    @Query("SELECT iq FROM InstrumentQuote iq WHERE iq.date <= :date AND iq.simbol IN :simbols ORDER BY iq.date ASC")
    List<InstrumentQuote> findByDateLessThanEqualAndSimbolIn(@Param("date") LocalDate date,  @Param("simbols") List<String> simbols,
    Pageable pageable);

    
}
