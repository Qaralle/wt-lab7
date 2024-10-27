package ru.itmo.model.dao;

import ru.itmo.model.Car;

import java.util.List;

public interface CarDao {
    List<Car> findCars(Long id, String name, Integer price, Integer count, Integer power);
    Long saveCar(Car car);
    Integer deleteCarById(Long id);
    Integer updateCar(Car car);
}
