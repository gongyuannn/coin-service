package com.example;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

// map Illegal Argument Exception to 422 status code & e.message
@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
    public Response toResponse(IllegalArgumentException e) {
        return Response.status(422)
                .entity(Map.of("error", e.getMessage()))
                .build();
    }
}
