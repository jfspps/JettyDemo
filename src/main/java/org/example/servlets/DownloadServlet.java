package org.example.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.constants.JettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class DownloadServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(DownloadServlet.class);

    // allow for path parameters when downloading files
    public static final String DOWNLOAD_ENDPOINT = "/download/*";

    private String filePath;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.debug("GET request received");
        getFilePath(request);

        try {

            attemptFileRetrieval(response);

        } catch (IOException ioException){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.debug("POST request received");
        getFilePath(request);

        try {

            attemptFileRetrieval(response);

        } catch (IOException ioException){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Builds the filePath, i.e. everything after {hostname}/, excluding query params ? and fragment #
     * @param request
     */
    private void getFilePath(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        logger.debug("Request URI: " + requestURI);

        filePath = requestURI.substring(
                (JettyServer.API_V1_ENDPOINT + "/download").length()
        );

        // these should be identical;
        // if only /api/v1/download is passed, then the first is blank and the latter is null
        logger.debug("Request for file path: " + filePath);
        logger.debug("Request path info: " + request.getPathInfo());
    }

    /**
     * Attempts to build a URL to the file or folder, and if found, then attempts to stream it.
     * @throws IOException
     */
    private void attemptFileRetrieval(HttpServletResponse response) throws IOException {
        if (filePath == null || filePath.isBlank()){
            throw new IOException("No file path information received, aborting...");
        }

        // wherever we are running this method, we need the absolute path to the file; if a URL cannot be
        // retrieved then return an HTTP 404
        URL fileUrl = this.getClass().getResource(filePath);

        if (fileUrl != null){
            logger.debug("File URL: " + fileUrl.getPath());
            buildInputStream(response, fileUrl);
        } else {
            logger.debug("Problem getting File Path from URL: file or folder may not exist...");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Builds an InputStream to buffer the physical file (based on the File URL) and then reads from this buffer passing
     * the data out to the response's OutputStream.
     * @param response
     * @param fileUrl
     */
    private void buildInputStream(HttpServletResponse response, URL fileUrl) {
        try (InputStream inputStream = fileUrl.openStream()) {

            if (inputStream == null){
                logger.debug("HTTP 404: could not find file with path: " + fileUrl.getPath());
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            response.setContentType("text/plain");
            response.setHeader("Content-disposition", "attachment; filename=" + filePath);

            OutputStream outputStream = response.getOutputStream();

            byte[] buffer = new byte[JettyServer.PACKET_SIZE];

            int numBytesRead;
            while ((numBytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, numBytesRead);
            }

        } catch (IOException e) {
            logger.error("Problem retrieving file with URL: " + fileUrl.getPath());
            throw new RuntimeException(e);
        }
    }
}
