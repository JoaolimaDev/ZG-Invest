package com.desafio_zg.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.desafio_zg.domain.model.UserTrade;

@Repository
public interface UserTradeRepository extends JpaRepository<UserTrade, Long> {
    @Query("SELECT U FROM UserTrade U WHERE U.data >= :datInicio AND U.data <= :dataFinal ORDER BY U.data ASC")
        List<UserTrade> findBetweendata(@Param("datInicio") LocalDate datInicio, 
                                        @Param("dataFinal") LocalDate dataFinal);

    Optional<UserTrade> findTopBydataOrderByDataDesc(LocalDate tradeDate);

    @Query(
    "SELECT U FROM UserTrade U WHERE U.data >= :datInicio AND U.data <= :dataFinal AND U.instrument IN :simbols ORDER BY U.data ASC")
    List<UserTrade> findBetweendataList(@Param("datInicio") LocalDate datInicio, 
                                     @Param("dataFinal") LocalDate dataFinal, 
                                     @Param("simbols") List<String> simbols);


    
}
