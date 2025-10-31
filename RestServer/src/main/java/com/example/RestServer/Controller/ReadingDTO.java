package com.example.RestServer.Controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadingDTO {
    Float temperature;
    Float pressure;
    Float humidity;
    Float co;
    Float no2;
    Float so2;
}
