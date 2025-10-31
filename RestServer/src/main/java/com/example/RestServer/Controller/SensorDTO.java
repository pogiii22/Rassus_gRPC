package com.example.RestServer.Controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class SensorDTO {
    @NotNull
    Float latitude;
    @NotNull
    Float longitude;
    @NotBlank
    String ip;
    @NotNull
    Integer port;
}
