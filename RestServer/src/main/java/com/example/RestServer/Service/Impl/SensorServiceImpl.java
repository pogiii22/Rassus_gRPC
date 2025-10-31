package com.example.RestServer.Service.Impl;

import com.example.RestServer.Controller.SensorDTO;
import com.example.RestServer.Domain.Sensor;
import com.example.RestServer.Repository.SensorRepository;
import com.example.RestServer.Service.SensorService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;


@Service
public class SensorServiceImpl implements SensorService {
    private static final Logger logger = Logger.getLogger(SensorServiceImpl.class.getName());

    @Autowired
    private SensorRepository sensorRepo;


    @Override
    public Sensor registerSensor(SensorDTO sensorDTO) {
        Sensor saved = new Sensor(sensorDTO.getLatitude(), sensorDTO.getLongitude(),
            sensorDTO.getIp(), sensorDTO.getPort());
        sensorRepo.save(saved);
        logger.info("[SERVICE] Registered sensor:" + saved);
        return saved;
    }

    @Override
    public Sensor findSensorById(Long Id) {
        logger.info("[SERVICE] searched for sensor with id " + Id);
        return sensorRepo.findById(Id)
                .orElseThrow(() -> new NoSuchElementException("That sensor does not exist"));
    }

    @Override
    public SensorDTO findNearestSensor(Long id) {
        List<Sensor> registeredSensors = sensorRepo.findAll();
        if(registeredSensors.isEmpty()){
            return null;
        }
        Sensor s1 = sensorRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Sensor not found"));
        final double R = 6371.0;
        Double minDistance = null;
        Sensor minimalDistanceSensor = null;
        for(Sensor s2: registeredSensors){
            if(s2.getId().equals(s1.getId())) continue;
            double dlong = Math.abs(s2.getLongitude() - s1.getLongitude());
            double dlat = Math.abs(s2.getLatitude()- s1.getLatitude());
            double a = Math.pow(Math.sin(dlat/2),2) + Math.cos(s1.getLatitude())* Math.cos(s2.getLatitude())*Math.pow(Math.sin(dlong/2),2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            double d = R*c;
            logger.info("[SERVICE] MinimalDistance and sensor in group of sensors" + minDistance + " " + s2.toString());
            logger.info("[SERVICE] Distance of sensor: " + d);

            if(minDistance == null){
                minDistance = d;
                minimalDistanceSensor = s2;
            } else if(d < minDistance){
                minDistance = d;
                minimalDistanceSensor = s2;
            }
        }

        if(minimalDistanceSensor == null){
            return null;
        }
        return new SensorDTO(minimalDistanceSensor.getLatitude(), minimalDistanceSensor.getLongitude(),
                minimalDistanceSensor.getIp(), minimalDistanceSensor.getPort());
    }

    @Override
    public List<Sensor> listAll() {
        return sensorRepo.findAll();
    }
}
