package com.example.RestServer.Repository;

import com.example.RestServer.Domain.Reading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReadingRepository extends JpaRepository<Reading, Long> {
    Optional<Reading> findBySensorIdAndId(Long sensorId, Long id);
}
