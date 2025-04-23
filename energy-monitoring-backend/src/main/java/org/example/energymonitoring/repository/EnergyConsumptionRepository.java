package org.example.energymonitoring.repository;

import org.example.energymonitoring.entity.EnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Repository
public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumption, UUID> {

    @Query("FROM EnergyConsumption e " +
            "WHERE e.device.id = :deviceId " +
            "AND e.timestamp >= :start " +
            "AND e.timestamp < :end")
    List<EnergyConsumption> findTotalConsumptionFromInterval(@Param("deviceId") UUID deviceId,
                                                             @Param("start") Instant start,
                                                             @Param("end") Instant end);

}
