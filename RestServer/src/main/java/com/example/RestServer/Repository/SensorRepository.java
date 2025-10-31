package com.example.RestServer.Repository;

import com.example.RestServer.Domain.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
}
