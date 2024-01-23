package com.harbour.clearview.api.application.resources.exceptions;

import com.harbour.clearview.api.datasource.dao.exceptions.CollectionNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CollectionNotFoundExceptionMapper implements ExceptionMapper<CollectionNotFoundException> {
    @Override
    public Response toResponse(CollectionNotFoundException e) {
        return Response
                .status(404)
                .entity(e.getMessage())
                .type("text/plain")
                .build();
    }
}
