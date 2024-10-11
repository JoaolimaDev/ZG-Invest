package com.desafio_zg.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desafio_zg.domain.model.UserTrade;

@Repository
public interface UserTradeRepository extends JpaRepository<UserTrade, Long> {

    List<UserTrade> findBydataLessThanEqual(LocalDate data, Pageable pageable);

}
