package ru.itmo.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CarRestClient extends AbstractClient {
    private static final String BASE_URL = "http://localhost:8083/api/cars";
    private final Client restClient = ClientBuilder.newClient();

    @Override
    protected void createCar() {
        CarParameters params = inputCarParameters();

        Response response = restClient.target(BASE_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, MediaType.APPLICATION_JSON));

        if (response.getStatus() == 201) {
            System.out.println("Автомобиль успешно создан (REST)");
        } else {
            System.out.println("Ошибка при создании автомобиля: "
                    + response.readEntity(String.class));
        }
    }

    @Override
    protected void searchCars() {
        Long id = inputId();
        CarParameters params = inputCarParameters();

        Response response = restClient.target(BASE_URL)
                .queryParam("id", id)
                .queryParam("name", params.name)
                .queryParam("price", params.price)
                .queryParam("count", params.count)
                .queryParam("power", params.power)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == 200) {
            System.out.println("Данные автомобиля (REST): " + response.readEntity(String.class));
        } else {
            System.out.println("Ошибка при получении автомобиля: " + response.readEntity(String.class));
        }
    }

    @Override
    protected void updateCar() {
        Long id = inputId();
        CarParameters params = inputCarParameters();

        Response response = restClient.target(BASE_URL + "/" + id)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(params, MediaType.APPLICATION_JSON));

        if (response.getStatus() == 204) {
            System.out.println("Автомобиль успешно обновлен (REST)");
        } else {
            System.out.println("Ошибка при обновлении автомобиля: " + response.readEntity(String.class));
        }
    }

    @Override
    protected void deleteCar() {
        Long id = inputId();

        Response response = restClient.target(BASE_URL + "/" + id)
                .request()
                .delete();

        if (response.getStatus() == 204) {
            System.out.println("Автомобиль успешно удален (REST)");
        } else {
            System.out.println("Ошибка при удалении автомобиля: " + response.readEntity(String.class));
        }
    }
}
