package ru.itmo.model.dao;

import ru.itmo.config.DataSourceProvider;
import ru.itmo.model.Car;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarRepository implements CarDao {
    private DataSource dataSource;

    public CarRepository() {
        try {
            InitialContext ctx = new InitialContext();
            this.dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/StudsDB");
        } catch (NamingException e) {
            this.dataSource = DataSourceProvider.getDataSource();
        }
    }

    @Override
    public List<Car> findCars(Long id, String name, Integer price, Integer count, Integer power) {
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
                results.add(buildCar(resultSet));
            }
        } catch (Exception e) {
            System.err.println("Exception in findCars: " + e.getMessage());
        }

        return results;
    }

    @Override
    public Long saveCar(Car car) {
        String sql = "INSERT INTO cars(name, count, price, power) VALUES (?, ?, ?, ?) RETURNING id";

        long id = -1L;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, car.getName());
            preparedStatement.setInt(2, car.getCount());
            preparedStatement.setInt(3, car.getPrice());
            preparedStatement.setInt(4, car.getPower());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    id = rs.getLong(1);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return id;
    }

    @Override
    public Integer deleteCarById(Long id) {
        String sql = "DELETE FROM cars WHERE id = ?";
        int affectedRows = 0;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            affectedRows = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return affectedRows;
    }

    @Override
    public Integer updateCar(Car car) {
        int affectedRows = -1;
        String sql = "UPDATE cars SET name = ?, count = ?, price = ?, power = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, car.getName());
            preparedStatement.setInt(2, car.getCount());
            preparedStatement.setInt(3, car.getPrice());
            preparedStatement.setInt(4, car.getPower());
            preparedStatement.setLong(5, car.getId());

            affectedRows = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return affectedRows;
    }

    private Car buildCar(ResultSet resultSet) throws SQLException {
        Car car = new Car();

        car.setId(resultSet.getLong("id"));
        car.setName(resultSet.getString("name"));
        car.setPrice(resultSet.getInt("price"));
        car.setCount(resultSet.getInt("count"));
        car.setPower(resultSet.getInt("power"));

        return car;
    }
}
