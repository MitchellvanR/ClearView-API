package com.harbour.clearview.api.application.resources.exceptions;

import com.harbour.clearview.api.datasource.dao.exceptions.NoTodoListsFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class NoTodoListsFoundExceptionMapper implements ExceptionMapper<NoTodoListsFoundException> {
    @Override
    public Response toResponse(NoTodoListsFoundException e) {
        return Response
                .status(404)
                .entity(e.getMessage())
                .type("text/plain")
                .build();
    }
}
