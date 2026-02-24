# Distributed Sensor System -- gRPC & REST

## Overview

This project is a solution for the first laboratory assignment in the
Distributed Systems course.\
The goal was to implement a centralized distributed system for
environmental sensor data collection and processing using REST and gRPC
communication.

The system simulates multiple sensors deployed in a geographic area.
Each sensor generates environmental readings, exchanges data with its
nearest neighbor, calibrates the results, and sends processed data to a
central server.

------------------------------------------------------------------------

## System Architecture

The system consists of two main components:

### 1. Server (REST API)

The server is responsible for:

-   Registering sensors
-   Storing sensor geographic locations
-   Determining the nearest neighbor using the Haversine formula
-   Storing calibrated sensor readings
-   Exposing REST endpoints for data retrieval

Communication format: **HTTP + JSON**

------------------------------------------------------------------------

### 2. Sensor (Client)

Each sensor:

-   Randomly selects a geographic location (within a predefined range)
-   Registers itself to the server
-   Generates readings from a CSV dataset
-   Requests its nearest neighbor from the server
-   Exchanges readings with the neighbor via gRPC
-   Calibrates readings (average value calculation)
-   Sends calibrated readings back to the server
-   Repeats the process continuously

Each sensor acts both as:

-   gRPC client (requesting neighbor readings)
-   gRPC server (responding to other sensors)

------------------------------------------------------------------------

## Data Flow

1.  Sensor starts\
2.  Registers to REST server\
3.  Requests nearest neighbor\
4.  Generates its own reading\
5.  Requests neighbor reading via gRPC\
6.  Calibrates data\
7.  Sends calibrated reading to server\
8.  Process repeats

------------------------------------------------------------------------

## Calibration Logic

Calibration is performed by calculating the arithmetic mean of
corresponding values:

    calibrated_value = (own_value + neighbor_value) / 2

Special cases: - If one value is missing or equals 0 → use the available
value - If both values are missing → set to null

------------------------------------------------------------------------

## Distance Calculation

The nearest neighbor is determined using the Haversine formula\
(Earth radius = 6371 km).

------------------------------------------------------------------------

## Technologies Used

-   Java 21
-   Spring Boot
-   gRPC
-   Protocol Buffers
-   Gradle
-   REST (HTTP + JSON)

------------------------------------------------------------------------

## Project Structure

    root/
    ├── server/    → REST server  
    └── sensor/    → Sensor client (REST + gRPC)

The server and sensor are implemented as two separate projects.

------------------------------------------------------------------------

## Running the Project

### Start the server

    cd server
    ./gradlew bootRun

### Start a sensor

    cd sensor
    ./gradlew run

To properly test the system, run at least two sensor instances.

------------------------------------------------------------------------

## Key Features

-   Combined use of REST and gRPC
-   Bidirectional sensor communication
-   Concurrent gRPC handling
-   Fault-tolerant neighbor communication
-   Proper HTTP status code handling
-   Protobuf message definitions for structured data transfer
