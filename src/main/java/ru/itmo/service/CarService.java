package ru.itmo.service;

import ru.itmo.model.Car;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface CarService {
    @WebMethod
    List<Car> searchCars(Long id, String name, Integer price, Integer count, Integer power);
}
