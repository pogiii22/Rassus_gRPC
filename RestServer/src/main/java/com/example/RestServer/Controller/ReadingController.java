package com.example.RestServer.Controller;

import com.example.RestServer.Domain.Reading;
import com.example.RestServer.Domain.Sensor;
import com.example.RestServer.Service.Impl.ReadingServiceImpl;
import com.example.RestServer.Service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/readings")
public class ReadingController {

    private static final Logger logger = Logger.getLogger(ReadingController.class.getName());


    @Autowired
    private ReadingService readingService;

    @GetMapping("list/{id}")
    public ResponseEntity<List<Reading>> listReadingsById(@PathVariable Long id){
        List<Reading> readings = readingService.listAllByID(id);
        if(readings.isEmpty()){
            return ResponseEntity.noContent().build();
        } else{
            return ResponseEntity.ok(readings);
        }
    }

    @PostMapping("new/{id}")
    public ResponseEntity<?> createNewReading(@RequestBody ReadingDTO readingDTO, @PathVariable Long id){
        logger.info("[CONTROLLER] this reading i got from server id " +id + ": " + readingDTO.toString());
        Reading reading = readingService.createReading(readingDTO, id);

        URI location = ServletUriComponentsBuilder
                .fromPath("/api/readings/sensor/{sensorId}/res/{id}")
                .scheme("http")
                .host("localhost")
                .port(8080)
                .buildAndExpand(id, reading.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("sensor/{sensorId}/res/{id}")
    public ResponseEntity<Reading> getSensor(@PathVariable Long sensorId, @PathVariable Long id){
        return readingService.getReading(sensorId, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

}
