package com.socks.repository;

import com.socks.model.Socks;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocksRepository extends JpaRepository<Socks, Long> {

    Optional<Socks> findByColorAndCottonPart(String color, Double cottonPart);

    @Query("SELECT SUM(s.quantity) FROM Socks s " +
        "WHERE s.color = :color " +
        "AND ((:comparison = 'equal' AND s.cottonPart = :cottonPart) " +
        "OR (:comparison = 'moreThan' AND s.cottonPart > :cottonPart) " +
        "OR (:comparison = 'lessThan' AND s.cottonPart < :cottonPart))")
    Integer findSocksQuantityByFilter(
        @Param("color") String color,
        @Param("comparison") String cottonPartComparison,
        @Param("cottonPart") Double cottonPart);

}
