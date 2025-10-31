package com.example.RestServer.Service.Impl;

import com.example.RestServer.Controller.ReadingDTO;
import com.example.RestServer.Domain.Reading;
import com.example.RestServer.Domain.Sensor;
import com.example.RestServer.Repository.ReadingRepository;
import com.example.RestServer.Service.ReadingService;
import com.example.RestServer.Service.SensorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class ReadingServiceImpl implements ReadingService {
    private static final Logger logger = Logger.getLogger(ReadingServiceImpl.class.getName());
    @Autowired
    private ReadingRepository readingRepo;
    @Autowired
    private SensorService sensorService;

    @Override
    public List<Reading> listAllByID(Long id) {
        Sensor sensor = sensorService.findSensorById(id);
        return sensor.getReadings();
    }

    @Override
    public Reading createReading(ReadingDTO readingDTO, Long id) {
        logger.info("[SERVICE] received Reading:" + readingDTO.toString());
        Sensor sensor = sensorService.findSensorById(id);

       Float temperature = readingDTO.getTemperature() != 0 ? readingDTO.getTemperature() : null;
       Float pressure = readingDTO.getPressure() != 0 ? readingDTO.getPressure() : null;
       Float humidity = readingDTO.getHumidity() != 0 ? readingDTO.getHumidity() : null;
       Float co = readingDTO.getCo() != 0 ? readingDTO.getCo() : null;
       Float no2 = readingDTO.getNo2() != 0 ? readingDTO.getNo2() : null;
       Float so2 = readingDTO.getSo2() != 0 ? readingDTO.getSo2() : null;

        Reading reading = new Reading(temperature, pressure, humidity, co, no2, so2, sensor);
        readingRepo.save(reading);
        logger.info("[SERVICE] New Reading:" + reading.toString());
        return reading;
    }

    @Override
    public Optional<Reading> getReading(Long sensorId, Long id) {
        return readingRepo.findBySensorIdAndId(sensorId, id);
    }
}
