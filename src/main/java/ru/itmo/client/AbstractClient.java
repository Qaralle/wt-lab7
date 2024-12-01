package ru.itmo.client;

import java.util.Scanner;

public abstract class AbstractClient {
    protected final Scanner scanner = new Scanner(System.in);

    public void run() {
        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Создать автомобиль");
            System.out.println("2. Поиск автомобиля");
            System.out.println("3. Обновить автомобиль");
            System.out.println("4. Удалить автомобиль");
            System.out.println("5. Выход");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createCar();
                    break;
                case "2":
                    searchCars();
                    break;
                case "3":
                    updateCar();
                    break;
                case "4":
                    deleteCar();
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

    protected abstract void createCar();

    protected abstract void searchCars();

    protected abstract void updateCar();

    protected abstract void deleteCar();

    protected Long inputId() {
        System.out.print("id: ");
        String idInput = scanner.nextLine();
        return idInput.isEmpty() ? null : Long.parseLong(idInput);
    }

    protected CarParameters inputCarParameters() {
        System.out.print("name: ");
        String name = scanner.nextLine();
        name = name.isEmpty() ? null : name;

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

    protected static class CarParameters {
        String name;
        Integer price;
        Integer count;
        Integer power;

        public CarParameters() {
        }

        CarParameters(String name, Integer price, Integer count, Integer power) {
            this.name = name;
            this.price = price;
            this.count = count;
            this.power = power;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Integer getPower() {
            return power;
        }

        public void setPower(Integer power) {
            this.power = power;
        }
    }
}