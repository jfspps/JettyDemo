package org.example.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * Demo servlet (remove, modify or disable at release)
 */
public class DemoServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(DemoServlet.class);

    public static final String DEMO_ENDPOINT = "/demo";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        logger.debug("GET request received");

        // protocol, server name, port number, and server path only
        StringBuffer stringBuffer = request.getRequestURL();

        // modify URL as desired
        stringBuffer.append("get");

        // (ensure HTTP header names are always valid)
        response.setHeader("url-requested", stringBuffer.toString());

        Date now = new Date();

        try {
            response.getWriter()
                    .append("Current Unix Timestamp: ")
                    .append(String.valueOf(now.getTime())
                    );

        } catch (IOException ioException){
            logger.error("Could not write response content");
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        validatePostRequestHeader(request, response);
        logger.debug("POST request header validated");

        handlePostRequest(request, response);
    }

    /**
     * Validates the request header and returns an error response if problems found
     * @param request
     * @param response
     */
    private void validatePostRequestHeader(HttpServletRequest request, HttpServletResponse response) {

        // ...perform other checks on the request header as desired...

    }

    /**
     * Process the POST request and builds the HTTP response
     * @param request
     * @param response
     * @throws IOException
     */
    private void handlePostRequest(HttpServletRequest request, HttpServletResponse response) {

        // ...do stuff...

        // protocol, server name, port number, and server path only
        StringBuffer stringBuffer = request.getRequestURL();

        // modify URL as desired
        stringBuffer.append("post");

        // (ensure HTTP header names are always valid)
        response.setHeader("url-requested", stringBuffer.toString());

        Date now = new Date();

        try {
            response.getWriter()
                    .append("Current Unix Timestamp: ")
                    .append(String.valueOf(now.getTime())
                    );

        } catch (IOException ioException){
            logger.error("Could not write response content");
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}