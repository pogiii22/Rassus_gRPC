package hr.fer.tel.rassus.examples;

public class Reading {
   // Temperature,Pressure,Humidity,CO,NO2,SO2,
    private Float temperature;
    private Float pressure;
    private Float humidity;
    private Float co;
    private Float no2;
    private Float so2;

    @Override
    public String toString() {
        return "Reading{" +
                "temperature=" + temperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", co=" + co +
                ", no2=" + no2 +
                ", so2=" + so2  + '}';
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getCo() {
        return co;
    }

    public void setCo(Float co) {
        this.co = co;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public Float getPressure() {
        return pressure;
    }

    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

    public Float getNo2() {
        return no2;
    }

    public void setNo2(Float no2) {
        this.no2 = no2;
    }

    public Float getSo2() {
        return so2;
    }

    public void setSo2(Float so2) {
        this.so2 = so2;
    }


    public Reading(){};

    public Reading(Float temperature, Float pressure, Float humidity, Float co,
                   Float no2, Float so2) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.co = co;
        this.no2 = no2;
        this.so2 = so2;
    }


}
