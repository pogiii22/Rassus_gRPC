package com.example.RestServer.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    Float latitude;
    Float longitude;
    String ip;
    Integer port;

    @OneToMany(mappedBy = "sensor")
    @JsonIgnore
    private List<Reading> readings = new ArrayList<>();

    public Sensor(Float latitude, Float longitude, String ip, Integer port) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.ip = ip;
        this.port = port;
    }
    @Override
    public String toString(){
        return "Sensor " + this.getId();
    }
}
