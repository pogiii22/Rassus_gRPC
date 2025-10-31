package com.example.RestServer.Service;

import com.example.RestServer.Controller.ReadingDTO;
import com.example.RestServer.Domain.Reading;

import java.util.List;
import java.util.Optional;

public interface ReadingService {
    List<Reading> listAllByID(Long id);
    Reading createReading(ReadingDTO readingDTO, Long id);
    Optional<Reading> getReading(Long sensorId, Long id);
}
