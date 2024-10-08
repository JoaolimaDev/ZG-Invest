package com.desafio_zg.domain.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.desafio_zg.domain.model.UserTrade;

@RepositoryRestResource(collectionResourceRel = "userTrades", path = "user_trades")
@Repository
public interface UserTradeRepository extends JpaRepository<UserTrade, Long> {
    
    List<UserTrade> findBydate(Date date);

}
