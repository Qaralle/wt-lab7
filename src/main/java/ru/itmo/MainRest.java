package ru.itmo;

import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.client.transport.Transport;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.*;
import org.uddi.v3_service.UDDIPublicationPortType;
import org.uddi.v3_service.UDDISecurityPortType;
import ru.itmo.config.CarRestApplication;

public class MainRest {
    private static final Logger logger = LoggerFactory.getLogger(MainRest.class);

    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    private static final String BUSINESS_NAME = "CarService";
    private static final String SERVICE_NAME = "CarServiceLab7";
    private static final String SERVICE_ENDPOINT = "http://localhost:8083/api/cars";


    public static void main(String[] args) throws Exception {
        int port = 8083;
        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ServletContainer servletContainer = new ServletContainer(new CarRestApplication());
        context.addServlet(new org.eclipse.jetty.servlet.ServletHolder(servletContainer), "/*");

        try {
            logger.info("Starting Jetty on port {}", port);
            server.start();
            logger.info("Jetty started successfully on port {}", port);

            registerService();

            server.join();
        } catch (Exception e) {
            logger.error("Error starting Jetty", e);
            throw e;
        }
    }

    private static void registerService() throws Exception {
        UDDIClient uddiClient = new UDDIClient("uddi.xml");
        Transport transport = uddiClient.getTransport("default");

        UDDISecurityPortType security = transport.getUDDISecurityService();
        UDDIPublicationPortType publish = transport.getUDDIPublishService();

        AuthToken token = auth(security, USER, PASSWORD);

        registerService(publish, token.getAuthInfo());
    }

    private static AuthToken auth(UDDISecurityPortType security, String user, String pass) throws Exception {
        GetAuthToken getAuthToken = new GetAuthToken();
        getAuthToken.setUserID(user);
        getAuthToken.setCred(pass);
        return security.getAuthToken(getAuthToken);
    }

    private static void registerService(UDDIPublicationPortType publish, String authInfo) throws Exception {
        BusinessEntity be = new BusinessEntity();
        Name beName = new Name();
        beName.setValue(BUSINESS_NAME);
        be.getName().add(beName);

        SaveBusiness sb = new SaveBusiness();
        sb.getBusinessEntity().add(be);
        sb.setAuthInfo(authInfo);

        BusinessDetail bd = publish.saveBusiness(sb);
        String businessKey = bd.getBusinessEntity().get(0).getBusinessKey();
        System.out.println("Зарегистрирован бизнес: " + BUSINESS_NAME + " c ключом: " + businessKey);

        BusinessService bs = new BusinessService();
        bs.setBusinessKey(businessKey);
        Name bsName = new Name();
        bsName.setValue(SERVICE_NAME);
        bs.getName().add(bsName);

        BindingTemplate bt = new BindingTemplate();
        AccessPoint ap = new AccessPoint();
        ap.setUseType("endPoint");
        ap.setValue(SERVICE_ENDPOINT);
        bt.setAccessPoint(ap);

        BindingTemplates bts = new BindingTemplates();
        bts.getBindingTemplate().add(bt);
        bs.setBindingTemplates(bts);

        SaveService ss = new SaveService();
        ss.setAuthInfo(authInfo);
        ss.getBusinessService().add(bs);
        ServiceDetail sd = publish.saveService(ss);

        String serviceKey = sd.getBusinessService().get(0).getServiceKey();
        System.out.println("Зарегистрирован сервис: " + SERVICE_NAME + " c ключом: " + serviceKey);
    }
}