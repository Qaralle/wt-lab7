package ru.itmo;

import ru.itmo.model.Car;
import ru.itmo.service.CarService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class TestClient {
    public static void main(String[] args) throws Exception {
        URL wsdlURL = new URL("http://localhost:8081/labs/ws/CarService?wsdl");
        QName qname = new QName("http://service.itmo.ru/", "CarService");
        Service service = Service.create(wsdlURL, qname);

        CarService carService = service.getPort(CarService.class);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("id: ");
            String idInput = scanner.nextLine();

            Long id = idInput.isEmpty() ? null : Long.parseLong(idInput);

            System.out.print("name: ");
            String name = scanner.nextLine();

            if (name.isEmpty()) {
                name = null;
            }

            System.out.print("price: ");
            String priceInput = scanner.nextLine();

            Integer price = priceInput.isEmpty() ? null : Integer.parseInt(priceInput);

            System.out.print("count: ");
            String countInput = scanner.nextLine();

            Integer count = countInput.isEmpty() ? null : Integer.parseInt(countInput);

            System.out.print("power: ");
            String powerInput = scanner.nextLine();

            Integer power = powerInput.isEmpty() ? null : Integer.parseInt(powerInput);

            List<Car> cars = carService.searchCars(id, name, price, count, power);

            if (cars.isEmpty()) {
                System.out.println("nothing found");
            } else {
                cars.forEach(System.out::println);
            }
        }
    }
}
