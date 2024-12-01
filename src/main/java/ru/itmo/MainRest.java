package ru.itmo;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.config.CarRestApplication;

public class MainRest {
    private static final Logger logger = LoggerFactory.getLogger(MainRest.class);

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
            server.join();
        } catch (Exception e) {
            logger.error("Error starting Jetty", e);
            throw e;
        }
    }
}