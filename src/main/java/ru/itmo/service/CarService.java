package ru.itmo.service;

import ru.itmo.model.Car;
import ru.itmo.service.status.OperationStatus;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface CarService {
    @WebMethod
    List<Car> searchCars(Long id, String name, Integer price, Integer count, Integer power);

    @WebMethod
    OperationStatus updateCar(Long id, String name, Integer price, Integer count, Integer power);

    @WebMethod
    OperationStatus deleteCar(Long id);

    @WebMethod
    Long createCar(String name, Integer price, Integer count, Integer power);
}
