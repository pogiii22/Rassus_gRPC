package hr.fer.tel.rassus.examples;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class ReadingsService extends ReadingServiceGrpc.ReadingServiceImplBase{
    private static final Logger logger = Logger.getLogger(ReadingsService.class.getName());

    private Reading currentReading;

    public Reading getCurrentReading() {
        return currentReading;
    }

    public void setCurrentReading(Reading currentReading) {
        this.currentReading = currentReading;
    }

    @Override
    public void getReading(Empty request, StreamObserver<NewReading> responseObserver) {
        NewReading response = NewReading.newBuilder().setTemperature(currentReading.getTemperature())
                .setPressure(currentReading.getPressure())
                .setHumidity(currentReading.getHumidity())
                .setCo(currentReading.getCo())
                .setNo2(currentReading.getNo2())
                .setSo2(currentReading.getTemperature()).build();


        responseObserver.onNext(response);
        logger.info("Responding with: " + response);
        responseObserver.onCompleted();
    }

}
