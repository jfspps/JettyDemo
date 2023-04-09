package org.example.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.servlets.helpers.ResourceList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

public class ResourceListServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(ResourceListServlet.class);

    public static final String RESOURCE_ENDPOINT = "/resource";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.debug("GET request received");

        // "target/classes/" becomes "classes" on packaging
        Set<String> filenameList = ResourceList.instance().getResourcesFileList("classes");

        try {

            for (String filename: filenameList) {
                response.getWriter()
                        .append("File found: ")
                        .append(filename)
                        .append("\n");
            }

        } catch (IOException ioException){
            logger.error("Could not write response content");
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
