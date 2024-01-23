package com.harbour.clearview.api.application.resources;

import com.harbour.clearview.api.application.dto.TodoDTO;
import com.harbour.clearview.api.application.services.TodoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/todos")
public class TodoResource {
    private TodoService todoService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTodo(TodoDTO todoDTO) {
        todoService.addTodo(todoDTO);
        return Response
                .created(URI.create("/" + todoDTO.getTitle()))
                .entity(todoDTO)
                .build();
    }

    @GET
    @Path("/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodo(@PathParam("title") String title) {
        return Response
                .ok()
                .entity(todoService.getTodo(title))
                .build();
    }

    @Inject
    public void setTodoService(TodoService todoService) {
        this.todoService = todoService;
    }
}
