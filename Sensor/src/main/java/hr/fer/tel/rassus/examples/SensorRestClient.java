package hr.fer.tel.rassus.examples;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.logging.Logger;

public class SensorRestClient {
    private static final Logger logger = Logger.getLogger(SensorRestClient.class.getName());

    private final HttpClient httpClient;
    private final String serverUrl;
    private final Gson gson;

    public SensorRestClient(String serverUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.serverUrl = serverUrl;
        this.gson = new Gson();
    }

    public boolean register(SimpleUnaryRPCServer server){
        try{
            String json = new Gson().toJson(Map.of(
                        "latitude", server.getLatitude(),
                        "longitude", server.getLongitude(),
                        "ip", server.getIp(),
                        "port", server.getPort()));

            HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(serverUrl + "/api/sensors/register"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("[HttpClient] Response code: " + response.statusCode()
            +"\nHeaders: " + response.headers()
            + "\n Id: " + response.body() + "\n");
            server.setId(Long.valueOf(response.body()));
            return true;
        }
        catch (Exception e){
            logger.warning("[HttpClient] Registration failed! Exception: " + e.toString());
            return false;
        }
    }

    public NeighborSensor findNeighbor(Long id){
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/api/sensors/neighbor/"+id))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("[HttpClient] Response code: " + response.statusCode()
            + "\n Neighbor: " + response.body() + "\n");
            String body = response.body();

            if(body == null || body.isBlank()){
                return null;
            }else{
                NeighborSensor sensor = new Gson().fromJson(body, NeighborSensor.class);
                return sensor;
            }
        }
        catch (Exception e){
            logger.warning("[HttpClient] Finding neighbor failed! Exception: " + e.toString());
            return null;
        }
    }

    public void sendData(Reading reading, Long id){
        try{
            String json = new Gson().toJson(Map.of( "temperature", reading.getTemperature(),
                    "pressure", reading.getTemperature(),
                    "humidity", reading.getHumidity(),
                    "co", reading.getCo(),
                    "no2", reading.getNo2(),
                    "so2", reading.getSo2()));

            logger.info("[HttpClient] Sending JSON: " + json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/api/readings/new/"+id))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("[HttpClient]Response code: " + response.statusCode() +
                    "\nHeaders: " + response.headers() + "\n");
        }
        catch (Exception e){
            logger.warning("[HttpClient] Sending data failed! Exception: " + e.toString());

        }
    }
}
