package org.example.servlets.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class ResourceList {

    private static final Logger logger = LoggerFactory.getLogger(ResourceList.class);

    public ResourceList(){}

    public static ResourceList instance(){
        return new ResourceList();
    }

    /**
     * Walks through a directory tree, starting from the parent directory given, and proceeds to
     * add the filename of any file discovered
     * @param dir Directory path. If preceded with "/" then this is taken as absolute to the system (host); otherwise,
     *            it is taken as relative to the JAR file
     * @return
     * @throws IOException
     */
    public Set<String> getResourcesFileList(String dir) throws IOException {

        Set<String> fileList = new HashSet<>();

        if (dir == null){
            throw new IOException("Missing a directory path");
        }

        if (dir.startsWith("/")){
            logger.debug("Absolute path requested: " + dir);
        } else
            logger.debug("Relative path requested: " + dir);

        // (this might not be necessary...) replace any URL friendly %2F with /
        String updatedDir = dir.replaceAll("%2F", "/");
        logger.debug("Updated path: " + updatedDir);

        Files.walkFileTree(Paths.get(updatedDir), new SimpleFileVisitor<>() {

            // define what to do when SimpleFileVisitor finds a file
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (!Files.isDirectory(file)) {

                    if (file.getParent() != null){
                        fileList.add(file.getParent() + "/" + file.getFileName().toString());
                    } else
                        fileList.add("/" + file.getFileName());

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
