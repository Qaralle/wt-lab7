package ru.itmo.service;

import ru.itmo.model.Car;

import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebService(serviceName = "CarService",
        endpointInterface = "ru.itmo.service.CarService",
        targetNamespace = "http://service.itmo.ru/")
public class CarServiceImpl implements CarService {

    @Override
    public List<Car> searchCars(Long id, String name, Integer price, Integer count, Integer power)  {
        DataSource dataSource;

        try {
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/StudsDB");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        List<Car> results = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM cars WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        Optional<Long> optionalId = Optional.ofNullable(id);
        Optional<String> optionalName = Optional.ofNullable(name);
        Optional<Integer> optionalPrice = Optional.ofNullable(price);
        Optional<Integer> optionalCount = Optional.ofNullable(count);
        Optional<Integer> optionalPower = Optional.ofNullable(power);

        optionalId.ifPresent(value -> {
            sql.append(" AND id = ?");
            parameters.add(value);
        });

        optionalName.ifPresent(value -> {
            sql.append(" AND name ILIKE ?");
            parameters.add(value);
        });

        optionalPrice.ifPresent(value -> {
            sql.append(" AND price = ?");
            parameters.add(value);
        });

        optionalCount.ifPresent(value -> {
            sql.append(" AND count = ?");
            parameters.add(value);
        });

        optionalPower.ifPresent(value -> {
            sql.append(" AND power = ?");
            parameters.add(value);
        });

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Car car = new Car();
                car.setId(resultSet.getLong("id"));
                car.setName(resultSet.getString("name"));
                car.setPrice(resultSet.getInt("price"));
                car.setCount(resultSet.getInt("count"));
                car.setPower(resultSet.getInt("power"));
                results.add(car);
            }
        } catch (Exception e) {
            System.err.println("Exception in searchCars: " + e.getMessage());
        }

        return results;
    }
}
