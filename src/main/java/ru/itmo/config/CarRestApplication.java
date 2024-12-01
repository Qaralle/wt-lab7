package ru.itmo.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import ru.itmo.ws.CarController;
import org.glassfish.jersey.server.ResourceConfig;

public class CarRestApplication extends ResourceConfig {
    public CarRestApplication() {
        register(CarController.class);

        packages("ru.itmo.ws");
        register(JacksonFeature.class);
    }
}