package org.example.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.servlets.helpers.ResourceList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

/**
 * Prints a list of files found in a directory either absolutely (precede with "/") or relatively to the JAR (do not
 * precede with "/").
 */
public class ResourceListServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(ResourceListServlet.class);

    public static final String RESOURCE_ENDPOINT = "/resource";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.debug("GET request received");

        String workingDirectory = request.getParameter("dir");

        if (workingDirectory == null || workingDirectory.isBlank()){
            logger.debug("No working directory query param or form data passed...aborting...");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

        Set<String> filenameList = ResourceList.instance().getResourcesFileList(workingDirectory);

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
