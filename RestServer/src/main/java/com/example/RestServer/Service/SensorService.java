package com.example.RestServer.Service;

import com.example.RestServer.Controller.SensorDTO;
import com.example.RestServer.Domain.Sensor;

import java.util.List;

public interface SensorService {
    Sensor registerSensor(SensorDTO sensorDTO);
    Sensor findSensorById(Long Id);
    SensorDTO findNearestSensor(Long id);
    List<Sensor> listAll();
}
