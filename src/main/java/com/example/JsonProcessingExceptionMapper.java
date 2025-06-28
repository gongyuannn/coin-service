package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

// handle invalid JSON
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {
    @Override
    public Response toResponse(JsonProcessingException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Invalid JSON: " + e.getOriginalMessage())
                .build();
    }
}
