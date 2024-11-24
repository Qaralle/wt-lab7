package ru.itmo.service;

import ru.itmo.model.Car;
import ru.itmo.service.exception.ArgumentException;
import ru.itmo.service.exception.DataException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface CarService {
    @WebMethod
    List<Car> searchCars(Long id, String name, Integer price, Integer count, Integer power) throws ArgumentException;

    @WebMethod
    void updateCar(Long id, String name, Integer price, Integer count, Integer power) throws DataException, ArgumentException;

    @WebMethod
    void deleteCar(Long id) throws DataException, ArgumentException;

    @WebMethod
    Long createCar(String name, Integer price, Integer count, Integer power) throws ArgumentException;
}
