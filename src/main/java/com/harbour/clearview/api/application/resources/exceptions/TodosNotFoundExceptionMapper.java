package com.harbour.clearview.api.application.resources.exceptions;

import com.harbour.clearview.api.datasource.dao.exceptions.TodosNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class TodosNotFoundExceptionMapper implements ExceptionMapper<TodosNotFoundException> {
    @Override
    public Response toResponse(TodosNotFoundException e) {
        return Response
                .status(404)
                .entity(e.getMessage())
                .type("text/plain")
                .build();
    }
}
