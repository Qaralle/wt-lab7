package ru.itmo;

import ru.itmo.service.CarServiceImpl;

import javax.xml.ws.Endpoint;

public class MainSoap {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8083/labs/ws/CarService",
                new CarServiceImpl());
    }
}