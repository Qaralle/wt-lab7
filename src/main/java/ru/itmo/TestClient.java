package ru.itmo;

import ru.itmo.model.Car;
import ru.itmo.service.CarService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.List;

public class TestClient {
    public static void main(String[] args) throws Exception {
        URL wsdlURL = new URL("http://localhost:8081/labs/ws/CarService?wsdl");
        QName qname = new QName("http://service.itmo.ru/", "CarService");
        Service service = Service.create(wsdlURL, qname);

        CarService carService = service.getPort(CarService.class);

        List<Car> cars = carService.searchCars(1L, null, null, null, null);

        if (cars != null) {
            System.out.println("Cars founded: " + cars);
        } else {
            System.out.println("Cars not found");
        }
    }
}
