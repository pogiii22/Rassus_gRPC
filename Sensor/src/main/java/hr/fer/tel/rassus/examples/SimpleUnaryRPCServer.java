package hr.fer.tel.rassus.examples;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.StatusRuntimeException;
import lombok.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SimpleUnaryRPCServer {
    private static final Logger logger = Logger.getLogger(SimpleUnaryRPCServer.class.getName());

    private Server server;
    private final ReadingsService service;
    private Long id;
    private final int port;
    private final String ip = InetAddress.getLocalHost().getHostAddress();
    private Float latitude;
    private Float longitude;
    private final Instant startTime;

    public ReadingsService getService() {
        return service;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public SimpleUnaryRPCServer(ReadingsService service, int port) throws UnknownHostException {
        this.service = service;
        this.port = port;
        this.startTime = Instant.now();
    }

    public long getWorkingTime() {
        return Duration.between(startTime, Instant.now()).getSeconds();
    }

    public void start() throws IOException {
        // Register the service
        server = ServerBuilder.forPort(port)
                .addService(service)
                .build()
                .start();
        logger.info("Server started on " + port);

        //  Clean shutdown of server in case of JVM shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Shutting down gRPC server since JVM is shutting down");
            try {
                SimpleUnaryRPCServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("Server shut down");
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }


    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    //Pri početku stvaranja nove instance promijeni port
    public static void main(String[] args) throws IOException, InterruptedException {
        final SimpleUnaryRPCServer server = new SimpleUnaryRPCServer(new ReadingsService(), 3001);

        SensorRestClient httpClient = new SensorRestClient("http://localhost:8080");
        server.start();

        server.setLatitude(randomLatitude());
        server.setLongitude(randomLongitude());

        boolean register = httpClient.register(server);
        if (!register) {
            server.stop();
        }

        logger.info("[MAIN] Server got id: " + server.getId());

        NeighborSensor neighbor = httpClient.findNeighbor(server.getId());
        SimpleUnaryRPCClient serverClient = null;

        if(neighbor != null){
            serverClient = new SimpleUnaryRPCClient(neighbor.getIp(), neighbor.getPort());
        }


        if(neighbor != null){
            while(true){
                Reading reading = getNewReading("readings[7].csv",server);
                Reading neighborRead = serverClient.getReading();
                if(neighborRead == null){
                    logger.info("Neigbor not online, sending my data!");
                    httpClient.sendData(reading, server.getId());
                } else{
                    logger.info("neighbor read: " + neighborRead.toString());
                    Float avgTemp = fixValue(neighborRead.getTemperature(),reading.getTemperature());
                    Float avgPressure = fixValue(neighborRead.getPressure(),reading.getPressure());
                    Float avgHumidity = fixValue(neighborRead.getHumidity(), reading.getHumidity());
                    Float avgCo = fixValue(neighborRead.getCo(), reading.getCo());
                    Float avgNo2 = fixValue(neighborRead.getNo2(), reading.getNo2());
                    Float avgSo2 = fixValue(neighborRead.getSo2(), reading.getSo2());
                    Reading fixedReading = new Reading(avgTemp, avgPressure, avgHumidity, avgCo, avgNo2, avgSo2);
                    logger.info("FixedReading: " + fixedReading.toString());
                    httpClient.sendData(fixedReading, server.getId());
                }

                waitFewSeconds(10000);

            }
        } else{
            while(true){
                Reading reading = getNewReading("readings[7].csv",server);
                httpClient.sendData(reading,server.getId());
                waitFewSeconds(10000);


            }
        }
    }

    public static void waitFewSeconds(long miliseconds){
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warning("Reading loop interrupted!");
            //break;
        }
    }

    public static Float fixValue(Float s1, Float s2){
        if(s1 != 0 & s2 != 0){
            return (s1+s2)/2;
        } else if(s1 != 0 & s2 == 0){
            return s1;
        } else if(s1 == 0 & s2 != 0){
            return s2;
        } else {
            return 0f;
        }
    }

    public static Reading getNewReading(String csvFile, SimpleUnaryRPCServer server){
        Reading reading = readFromCvs(csvFile, server.getWorkingTime());
        server.getService().setCurrentReading(reading);
        logger.info("[MAIN] Reading: " + reading.toString() + "\n");
        return reading;
    }

    public static Float randomLongitude() {
        float min = 15.87f;
        float max = 16.00f;

        float random = (float) ThreadLocalRandom.current().nextDouble(min, max);
        float rounded = Math.round(random * 100f) / 100f;

        logger.info("[MAIN]Random Longitude: " + rounded);
        return rounded;
    }

    public static Float randomLatitude() {
        float min = 45.75f;
        float max = 45.85f;

        float random = (float) ThreadLocalRandom.current().nextDouble(min, max);
        float rounded = Math.round(random * 100f) / 100f;

        logger.info("[MAIN]Random latitude: " + rounded);
        return rounded;
    }

    public static Reading readFromCvs(String csvFile, long workingTime) {
        String splitSign = ",";
        long randomLine = (workingTime % 100) + 1;
        String line = null;
        Reading reading = new Reading();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            int currentLine = 0;
            while ((line = br.readLine()) != null) {
                currentLine++;
                if (currentLine == randomLine) {
                    String[] values = line.split(splitSign, -1);
                    for (int i = 0; i < values.length; i++) {
                        if (values[i].isEmpty() || values[i].equals("0")) {
                            values[i] = "0";
                        }
                    }
                    logger.info("[MAIN] Line number: " + randomLine);
                    reading.setTemperature(Float.parseFloat(values[0]));
                    reading.setPressure(Float.parseFloat(values[1]));
                    reading.setHumidity(Float.parseFloat(values[2]));
                    reading.setCo(Float.parseFloat(values[3]));
                    reading.setNo2(Float.parseFloat(values[4]));
                    reading.setSo2(Float.parseFloat(values[5]));
                    break;
                }
            }
        } catch (IOException e) {
            logger.warning("[MAIN] Reading from csv failed! Exception: " + e.toString());
        }

        return reading;
    }
}