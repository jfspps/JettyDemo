package org.example;

import org.eclipse.jetty.http.HttpCookie;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.example.constants.JettyServer;
import org.example.servlets.DemoServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demo showing how to integrate and set up Jetty
 */
public class JettyDemo {

    private final Logger logger = LoggerFactory.getLogger(JettyDemo.class);

    public static final String LOG_LEVEL = "debug";
    public static final boolean PRODUCTION_MODE = false;

    private Server jettyServer;

    public static void main(String[] args) {

        JettyDemo jettyDemo = new JettyDemo();
        jettyDemo.startJetty();

    }

    private void startJetty() {

        jettyServer = new Server();

        // Add a ServerConnector to accept connections from clients and facilitate data transfer; can use this to set up
        // connectors for http or https specifically if needed
        ServerConnector serverConnector = new ServerConnector(jettyServer);
        serverConnector.setPort(JettyServer.PORT);

        jettyServer.addConnector(serverConnector);

        // Configure session persistence (session cache is by default in-memory, hence is cleared on service reboot; alternatively,
        // session cache can be saved to a local file or database)
        SessionHandler sessions = new SessionHandler();

        // Cookies in requests
        if (PRODUCTION_MODE) {
            logger.info("Running in production mode");

            // enables all requests from first-party and third-party to include our cookie
            sessions.setSameSite(HttpCookie.SameSite.NONE);

            // consequently to above (potential CSRF), mark cookie as secure
            sessions.getSessionCookieConfig().setSecure(true);

            // over https only
            sessions.setSecureRequestOnly(true);
        } else {
            //	Production mode "false"
            logger.warn("Running in development/test mode");

            // enable top-level requests from first-party and third-party to include our cookie (the "default")
            sessions.setSameSite(HttpCookie.SameSite.LAX);
            sessions.setSecureRequestOnly(false);
            sessions.getSessionCookieConfig().setSecure(false);
        }

        // Cookie general configuration: the cookie name, (if relevant) the cookie domain name and cookie lifetime (seconds)
        sessions.getSessionCookieConfig().setName(JettyServer.SESSION_COOKIE_NAME);
        if (!JettyServer.COOKIE_DOMAIN.contentEquals("cookie.domain")) {
            sessions.getSessionCookieConfig().setDomain(JettyServer.COOKIE_DOMAIN);
        }
        sessions.getSessionCookieConfig().setMaxAge(JettyServer.COOKIE_LIFETIME_IN_SECONDS);

        // build a collection of context handlers (machinery for handling HTTP requests and sending responses)
        ContextHandlerCollection handlers = new ContextHandlerCollection();

        // End-points and their servlets
        ServletContextHandler servlets = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servlets.setSessionHandler(sessions);

        servlets.addServlet(new ServletHolder(new DemoServlet()), JettyServer.API_V1_ENDPOINT + DemoServlet.DEMO_ENDPOINT);

        // add the servlets to the context handlers list
        handlers.addHandler(servlets);

        // ...Jetty v10 onwards already uses SLF4j for logging, so we leave these defaults alone...
        RequestLogHandler logHandler = new RequestLogHandler();

        // make sure servlet logs are sent to the console
        logHandler.setHandler(servlets);
        jettyServer.setHandler(logHandler);

        // set limit of form size in POST requests to 2147483647 bytes (guard against DoS)
        jettyServer.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", JettyServer.REQUEST_FORM_MAX_SIZE_IN_BYTES);

        try {
            jettyServer.start();
            logger.info("Jetty started. Listening for requests..");

            // throws InterruptedException so this becomes the last call
            jettyServer.join();

        } catch (Exception e){
            logger.error("Problem starting Jetty...");
            logger.error(e.getMessage());
        }
    }
}
