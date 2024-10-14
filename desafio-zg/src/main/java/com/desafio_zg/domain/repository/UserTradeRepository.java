package com.desafio_zg.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desafio_zg.domain.model.UserTrade;

@Repository
public interface UserTradeRepository extends JpaRepository<UserTrade, Long> {
    @Query(
    "SELECT U FROM UserTrade U WHERE U.data >= :datInicio AND U.data <= :dataFinal ORDER BY U.data ASC")
    List<UserTrade> findBetweendataList(@Param("datInicio") LocalDate datInicio, 
                                     @Param("dataFinal") LocalDate dataFinal);

}
