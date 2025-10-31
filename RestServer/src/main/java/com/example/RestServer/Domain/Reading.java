package com.example.RestServer.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Reading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float temperature;
    private Float pressure;
    private Float humidity;
    private Float co;
    private Float no2;
    private Float so2;

    @ManyToOne
    @JsonIgnore
    private Sensor sensor;

    public Reading(Float temperature, Float pressure,
                   Float humidity, Float co, Float no2,
                   Float so2, Sensor sensor) {
        this.so2 = so2;
        this.no2 = no2;
        this.co = co;
        this.humidity = humidity;
        this.pressure = pressure;
        this.temperature = temperature;
        this.sensor = sensor;
    }
}
