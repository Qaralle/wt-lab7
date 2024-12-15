package ru.itmo.client;

import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.client.transport.Transport;
import org.uddi.api_v3.*;
import org.uddi.v3_service.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CarRestClient extends AbstractClient {
    private static final String SERVICE_NAME = "CarServiceLab-100";

    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    private String baseUrl;
    private final Client restClient = ClientBuilder.newClient();

    public CarRestClient() throws IllegalAccessException {
        this.baseUrl = lookupServiceEndpointFromJuddi(SERVICE_NAME);

        if (this.baseUrl == null) {
            throw new IllegalAccessException("Service not found");
        }
    }

    @Override
    protected void createCar() {
        CarParameters params = inputCarParameters();

        Response response = restClient.target(baseUrl)
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

        Response response = restClient.target(baseUrl)
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

        Response response = restClient.target(baseUrl + "/" + id)
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

        Response response = restClient.target(baseUrl + "/" + id)
                .request()
                .delete();

        if (response.getStatus() == 204) {
            System.out.println("Автомобиль успешно удален (REST)");
        } else {
            System.out.println("Ошибка при удалении автомобиля: " + response.readEntity(String.class));
        }
    }

    private String lookupServiceEndpointFromJuddi(String serviceName) {
        try {
            UDDIClient uddiClient = new UDDIClient("uddi.xml");
            Transport transport = uddiClient.getTransport("default");

            UDDISecurityPortType security = transport.getUDDISecurityService();
            UDDIInquiryPortType inquiry = transport.getUDDIInquiryService();

            GetAuthToken getAuthToken = new GetAuthToken();
            getAuthToken.setUserID(USER);
            getAuthToken.setCred(PASSWORD);
            AuthToken token = security.getAuthToken(getAuthToken);

            FindService fs = new FindService();
            fs.setAuthInfo(token.getAuthInfo());
            Name searchName = new Name();
            searchName.setValue(serviceName);
            fs.getName().add(searchName);

            ServiceList serviceList = inquiry.findService(fs);
            if (serviceList.getServiceInfos() != null &&
                    !serviceList.getServiceInfos().getServiceInfo().isEmpty()) {
                ServiceInfo si = serviceList.getServiceInfos().getServiceInfo().get(0);
                String serviceKey = si.getServiceKey();

                GetServiceDetail gsd = new GetServiceDetail();
                gsd.setAuthInfo(token.getAuthInfo());
                gsd.getServiceKey().add(serviceKey);

                ServiceDetail sd = inquiry.getServiceDetail(gsd);

                if (sd.getBusinessService() != null && !sd.getBusinessService().isEmpty()) {
                    BusinessService service = sd.getBusinessService().get(0);

                    if (service.getBindingTemplates() != null &&
                            !service.getBindingTemplates().getBindingTemplate().isEmpty()) {
                        BindingTemplate bt = service.getBindingTemplates().getBindingTemplate().get(0);

                        return bt.getAccessPoint().getValue();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}