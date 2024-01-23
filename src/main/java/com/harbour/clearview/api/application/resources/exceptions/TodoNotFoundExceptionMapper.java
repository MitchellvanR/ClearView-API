package com.harbour.clearview.api.application.resources.exceptions;

import com.harbour.clearview.api.datasource.dao.exceptions.TodoNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TodoNotFoundExceptionMapper implements ExceptionMapper<TodoNotFoundException> {
    @Override
    public Response toResponse(TodoNotFoundException e) {
        return Response
                .status(404)
                .entity(e.getMessage())
                .type("text/plain")
                .build();
    }
}
