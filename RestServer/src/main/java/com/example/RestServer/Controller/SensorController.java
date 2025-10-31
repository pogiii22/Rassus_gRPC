package com.example.RestServer.Controller;

import com.example.RestServer.Domain.Sensor;
import com.example.RestServer.Service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/sensors")
public class SensorController {
    @Autowired
    private SensorService sensorService;

    @GetMapping("listAll")
    public ResponseEntity<List<Sensor>> listAll(){
        return ResponseEntity.ok(sensorService.listAll());
    }

    @PostMapping("register")
    public ResponseEntity<?> registerSensor(@RequestBody SensorDTO sensorDTO){
        Sensor sensor = sensorService.registerSensor(sensorDTO);

        URI location = ServletUriComponentsBuilder
                .fromPath("/api/sensors/find/{id}")
                .scheme("http")               // http ili https
                .host("localhost")
                .port(8080)
                .buildAndExpand(sensor.getId())
                .toUri();

        return ResponseEntity.created(location).body(sensor.getId());
    }

    @GetMapping("find/{id}")
    public ResponseEntity<Sensor> getSensor(@PathVariable Long id){
        return ResponseEntity.ok(sensorService.findSensorById(id));
    }

    @GetMapping("neighbor/{id}")
    public ResponseEntity<SensorDTO> getNeigbhorSensor(@PathVariable Long id){
        SensorDTO neighbour = sensorService.findNearestSensor(id);
        if (neighbour != null) {
            return ResponseEntity.ok(neighbour);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
