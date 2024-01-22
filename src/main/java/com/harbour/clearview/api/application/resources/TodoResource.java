package com.harbour.clearview.api.application.resources;

import com.harbour.clearview.api.application.services.TodoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class TodoResource {
    private TodoService todoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response helloWorld() {
        return Response
                .ok()
                .entity(todoService.getHardcodedTodos())
                .build();
    }

    @Inject
    public void setTodoService(TodoService todoService) {
        this.todoService = todoService;
    }
}
