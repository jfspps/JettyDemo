package org.example.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.servlets.helpers.ResourceList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

public class PrivResourceListServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(PrivResourceListServlet.class);

    public static final String RESOURCE_ENDPOINT = "/resource/priv";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.debug("GET request received");

        // "target/classes/priv" becomes "classes/priv" on packaging
        Set<String> filenameList = ResourceList.instance().getResourcesFileList("classes/priv");

        try {

            for (String filename: filenameList) {
                response.getWriter()
                        .append("Private File found: ")
                        .append(filename)
                        .append("\n");
            }

        } catch (IOException ioException){
            logger.error("Could not write response content");
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
