package org.example.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public class ResourceListServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(ResourceListServlet.class);

    public static final String RESOURCE_ENDPOINT = "/resource";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.debug("GET request received");

        // et everything under the classpath (normally equivalent to that defined in /target/classes)
        Set<String> filenameList = getResourcesFileList("");

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

    /**
     * Walks through a directory tree, starting from the parent directory given, and proceeds to
     * add the filename of any file discovered
     * @param dir
     * @return
     * @throws IOException
     */
    private Set<String> getResourcesFileList(String dir) throws IOException {

        Set<String> fileList = new HashSet<>();

        Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (!Files.isDirectory(file)) {
                    fileList.add(file.getFileName().toString());
                }
                return FileVisitResult.CONTINUE;
            }

        });

        if (fileList.isEmpty()){
            logger.debug("Could not find any files");
        } else
            logger.debug("Found " + fileList.size() + " files");

        return fileList;
    }
}
