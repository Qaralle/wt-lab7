package ru.itmo;

import ru.itmo.model.Car;
import ru.itmo.service.CarService;
import ru.itmo.service.exception.ArgumentException;
import ru.itmo.service.exception.DataException;
import ru.itmo.service.status.OperationStatus;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class TestClient {
    public static void main(String[] args) throws Exception {
        URL wsdlURL = new URL("http://localhost:8083/labs/ws/CarService?wsdl");
        QName qname = new QName("http://service.itmo.ru/", "CarService");
        Service service = Service.create(wsdlURL, qname);

        CarService carService = service.getPort(CarService.class);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите операцию:");
            System.out.println("1. Поиск автомобилей");
            System.out.println("2. Создать автомобиль");
            System.out.println("3. Обновить автомобиль");
            System.out.println("4. Удалить автомобиль");
            System.out.println("5. Выход");
            System.out.print("> ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    Long id = inputId(scanner);
                    CarParameters params = inputCarParameters(scanner);

                    try {
                        List<Car> cars = carService.searchCars(id, params.name, params.price, params.count, params.power);

                        if (cars.isEmpty()) {
                            System.out.println("Ничего не найдено");
                        } else {
                            cars.forEach(System.out::println);
                        }
                    } catch (ArgumentException argumentException) {
                        System.out.println("Ошибка входных параметров: "
                                + argumentException.getFaultInfo().getArgumentName()
                                + ":"
                                + argumentException.getFaultInfo().getMessage());
                    }

                    break;

                case "2":
                    CarParameters newCarParams = inputCarParameters(scanner);

                    try {
                        Long newId = carService.createCar(newCarParams.name,
                                newCarParams.price,
                                newCarParams.count,
                                newCarParams.power);

                        System.out.println("Создан автомобиль с id: " + newId);
                    } catch (ArgumentException argumentException) {
                        System.out.println("Ошибка входных параметров: "
                                + argumentException.getFaultInfo().getArgumentName()
                                + ":"
                                + argumentException.getFaultInfo().getMessage());
                    }

                    break;

                case "3":
                    Long updateId = inputId(scanner);
                    CarParameters updateParams = inputCarParameters(scanner);

                    try {
                        carService.updateCar(updateId,
                                updateParams.name,
                                updateParams.price,
                                updateParams.count,
                                updateParams.power);

                        System.out.println("Автомобиль успешно обновлен");
                    } catch (ArgumentException argumentException) {
                        System.out.println("Ошибка входных параметров: "
                                + argumentException.getFaultInfo().getArgumentName()
                                + ":"
                                + argumentException.getMessage());
                    } catch (DataException dataException) {
                        System.out.println("Ошибка данных: "
                                + dataException.getFaultInfo().getEntityId()
                                + ":"
                                + dataException.getMessage());
                    }

                    break;

                case "4":
                    Long deleteId = inputId(scanner);

                    try {
                        carService.deleteCar(deleteId);

                        System.out.println("Автомобиль успешно удален");
                    } catch (ArgumentException argumentException) {
                        System.out.println("Ошибка входных параметров: "
                                + argumentException.getFaultInfo().getArgumentName()
                                + ":"
                                + argumentException.getMessage());
                    } catch (DataException dataException) {
                        System.out.println("Ошибка данных: "
                                + dataException.getFaultInfo().getEntityId()
                                + ":"
                                + dataException.getMessage());
                    }

                    break;

                case "5":
                    System.out.println("Выход из программы");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Некорректный выбор. Попробуйте снова.");
                    break;
            }
        }
    }

    private static Long inputId(Scanner scanner) {
        System.out.print("id: ");
        String idInput = scanner.nextLine();
        return idInput.isEmpty() ? null : Long.parseLong(idInput);
    }

    private static CarParameters inputCarParameters(Scanner scanner) {
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

        return new CarParameters(name, price, count, power);
    }

    private static class CarParameters {
        String name;
        Integer price;
        Integer count;
        Integer power;

        CarParameters(String name, Integer price, Integer count, Integer power) {
            this.name = name;
            this.price = price;
            this.count = count;
            this.power = power;
        }
    }
}
