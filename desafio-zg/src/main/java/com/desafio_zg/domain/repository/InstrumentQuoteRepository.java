package com.desafio_zg.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.desafio_zg.domain.model.InstrumentQuote;

@RepositoryRestResource(collectionResourceRel = "instrumentQuotes", path = "instrument_quotes")
@Repository
public interface InstrumentQuoteRepository extends JpaRepository<InstrumentQuote, Long> {

    @Query("SELECT iq FROM InstrumentQuote iq WHERE iq.date <= :date AND iq.simbol IN :simbols")
    List<InstrumentQuote> findByDateLessThanEqualAndSimbolIn(@Param("date") LocalDate date,  @Param("simbols") List<String> simbols);

    
}
