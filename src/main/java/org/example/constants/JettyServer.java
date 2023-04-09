package org.example.constants;

public class JettyServer {

    // pick anything not already in use (0 - 1023 are well-known ports)
    public static final int PORT = 1123;

    public static final int PACKET_SIZE = 1024;

    public static final Integer REQUEST_FORM_MAX_SIZE_IN_BYTES = Integer.MAX_VALUE;

    public static final String COOKIE_DOMAIN = "cookie.domain";

    public static final String SESSION_COOKIE_NAME = "JSESSIONID_JETTY_DEMO";
    public static final int COOKIE_LIFETIME_IN_SECONDS = 84600;

    // intended for RESTful clients (add v2, v3... whenever required)
    public static final String API_V1_ENDPOINT = "/api/v1";

    // query parameter names (or form data field names)
    public static final String REDIRECT_QUERY_PARAMETER = "return";
    public static final String ERROR_QUERY_PARAMETER = "err";
}
