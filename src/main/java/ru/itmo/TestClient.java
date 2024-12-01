package ru.itmo;

import ru.itmo.client.CarRestClient;

public class TestClient {
    public static void main(String[] args) throws Exception {
        CarRestClient client = new CarRestClient();

        client.run();
    }
}