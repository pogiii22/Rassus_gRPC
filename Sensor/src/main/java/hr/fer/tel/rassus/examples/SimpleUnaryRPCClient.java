package hr.fer.tel.rassus.examples;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class SimpleUnaryRPCClient {

  private static final Logger logger = Logger.getLogger(SimpleUnaryRPCClient.class.getName());

  private final ManagedChannel channel;
  private final ReadingServiceGrpc.ReadingServiceBlockingStub readingBlockingStub;

  public SimpleUnaryRPCClient(String host, int port) {
    this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

    readingBlockingStub = ReadingServiceGrpc.newBlockingStub(channel);
  }

  public void stop() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public Reading getReading() {
      try {
          NewReading response = readingBlockingStub.getReading(Empty.getDefaultInstance());
          logger.info("Received: " + response);
          Reading neighborRead = new Reading(response.getTemperature(), response.getPressure(), response.getHumidity()
                  , response.getCo(), response.getNo2(), response.getSo2());
          return neighborRead;
      } catch (StatusRuntimeException e) {
          logger.info("RPC failed: " + e.getMessage());
          return null;
      }
  }}


