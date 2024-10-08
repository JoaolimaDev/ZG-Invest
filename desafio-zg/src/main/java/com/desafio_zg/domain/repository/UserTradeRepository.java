package com.desafio_zg.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.desafio_zg.domain.model.UserTrade;

@RepositoryRestResource(collectionResourceRel = "userTrades", path = "user_trades")
public interface UserTradeRepository extends JpaRepository<UserTrade, Long> {
    
}
