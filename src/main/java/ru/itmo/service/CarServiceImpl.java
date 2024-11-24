package ru.itmo.service;

import ru.itmo.model.Car;
import ru.itmo.model.dao.CarDao;
import ru.itmo.model.dao.CarRepository;
import ru.itmo.service.exception.ArgumentException;
import ru.itmo.service.exception.DataException;
import ru.itmo.service.validation.CarValidator;

import javax.jws.WebService;
import java.util.List;

@WebService(serviceName = "CarService",
        endpointInterface = "ru.itmo.service.CarService",
        targetNamespace = "http://service.itmo.ru/")
public class CarServiceImpl implements CarService {
    private final CarDao carRepository;

    public CarServiceImpl() {
        this.carRepository = new CarRepository();
    }

    @Override
    public List<Car> searchCars(Long id,
                                String name,
                                Integer price,
                                Integer count,
                                Integer power) throws ArgumentException {
        CarValidator.validateCarArguments(id, name, price, count, power);
        return carRepository.findCars(id, name, price, count, power);
    }

    @Override
    public void updateCar(Long id,
                          String name,
                          Integer price,
                          Integer count,
                          Integer power) throws DataException, ArgumentException {
        CarValidator.validateCarArguments(id, name, price, count, power);

        Car car = new Car();

        car.setId(id);
        car.setName(name);
        car.setPrice(price);
        car.setCount(count);
        car.setPower(power);

        if (carRepository.updateCar(car) <= 0) {
            throw new DataException(id, "Entity not found");
        }
    }

    @Override
    public void deleteCar(Long id) throws DataException, ArgumentException {
        CarValidator.validateCarArguments(id, null, null, null, null);

        if (carRepository.deleteCarById(id) <= 0) {
            throw new DataException(id, "Entity not found");
        }
    }

    @Override
    public Long createCar(String name,
                          Integer price,
                          Integer count,
                          Integer power) throws ArgumentException {
        CarValidator.validateCarArguments(null, name, price, count, power);

        Car car = new Car();

        car.setName(name);
        car.setPrice(price);
        car.setCount(count);
        car.setPower(power);

        return carRepository.saveCar(car);
    }
}
