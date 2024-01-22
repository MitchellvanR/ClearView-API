package com.harbour.clearview.api.application.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/hello")
public class HelloResource {

    @GET
    public String helloWorld() {
        return "Hello world!";
    }
}
