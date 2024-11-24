package ru.itmo.service.validation;

import ru.itmo.service.exception.ArgumentException;

public class CarValidator {
    public static void validateCarArguments(Long id,
                                      String name,
                                      Integer price,
                                      Integer count,
                                      Integer power) throws ArgumentException {
        validateIfPresent(id, CarValidator::validateId);
        validateIfPresent(name, CarValidator::validateName);
        validateIfPresent(price, CarValidator::validatePrice);
        validateIfPresent(count, CarValidator::validateCount);
        validateIfPresent(power, CarValidator::validatePower);
    }

    private static  <T> void validateIfPresent(T value, ValidationConsumer<T> validator) throws ArgumentException {
        if (value != null) {
            validator.accept(value);
        }
    }

    private static  void validateId(Long id) throws ArgumentException {
        if (id < 0) {
            throw new ArgumentException("id", "must be > 0");
        }
    }

    private static  void validateName(String name) throws ArgumentException {
        if (name.isBlank()) {
            throw new ArgumentException("name", "must not be empty");
        }
    }

    private static  void validatePrice(Integer price) throws ArgumentException {
        if (price <= 0) {
            throw new ArgumentException("price", "must be > 0");
        }
    }

    private static  void validateCount(Integer count) throws ArgumentException {
        if (count < 0) {
            throw new ArgumentException("count", "must be >= 0");
        }
    }

    private static  void validatePower(Integer power) throws ArgumentException {
        if (power <= 0) {
            throw new ArgumentException("power", "must be > 0");
        }
    }

    @FunctionalInterface
    private interface ValidationConsumer<T> {
        void accept(T t) throws ArgumentException;
    }
}
