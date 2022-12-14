package com.github.nsbazhenov.skytec.data.status;

/**
 * List of http codes.
 *
 * @author Bazhenov Nikita
 *
 */
public enum HttpStatusCode {
    //2xx: Success
    OK(200, "OK"),
    //3xx: Redirection
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    //4xx: Client error
    BAD_REQUEST(400, "Bad Request"),
    //5xx: Server error
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int value;
    private final String description;

    HttpStatusCode(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return value + " " + description;
    }
}
