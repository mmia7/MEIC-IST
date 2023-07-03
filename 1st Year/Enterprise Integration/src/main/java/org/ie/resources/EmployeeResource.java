package org.ie.resources;

import java.net.URI;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import org.ie.entities.Employee;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("employee")
public class EmployeeResource {
    @Inject
    io.vertx.mutiny.mysqlclient.MySQLPool client;

    @GET
    @Path("/all")
    public Multi<Employee> get() {
        return Employee.findAll(client);
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSingle(@PathParam Long id) {
        return Employee.findById(client, id)
                .onItem().transform(employee -> employee != null ? Response.ok(employee) : Response.status(Status.NOT_FOUND)) 
                .onItem().transform(ResponseBuilder::build); 
    }

    @POST
    @Path("/add/employee")
    public Uni<Response> create(Employee employee) {
        return employee.save(client)
        .onItem().transform(id -> URI.create("/employee/" + id))
        .onItem().transform(uri -> Response.created(uri).build());
    }

    @DELETE
    @Path("/remove/employee/{id}")
    public Uni<Response> delete(@PathParam Long id) {
        return Employee.delete(client, id)
        .onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
        .onItem().transform(status -> Response.status(status).build());
    }
}