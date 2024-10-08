package com.desafio_zg.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.desafio_zg.domain.model.InstrumentQuote;

@RepositoryRestResource(collectionResourceRel = "instrumentQuotes", path = "instrument_quotes")
@Repository
public interface InstrumentQuoteRepository extends JpaRepository<InstrumentQuote, Long> {

}
