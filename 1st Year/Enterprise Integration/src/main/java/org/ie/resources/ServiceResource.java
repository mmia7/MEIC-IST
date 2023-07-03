package org.ie.resources;

import java.net.URI;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.ie.entities.Service;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("service")
public class ServiceResource {
    @Inject
    io.vertx.mutiny.mysqlclient.MySQLPool client;

    @GET
    @Path("/all")
    public Multi<Service> get() {
        return Service.findAll(client);
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSingle(@PathParam Long id) {
        return Service.findById(client, id)
                .onItem().transform(service -> service != null ? Response.ok(service) : Response.status(Status.NOT_FOUND))
                .onItem().transform(ResponseBuilder::build);
    }

    @POST
    @Path("/add/service")
    public Uni<Response> create(Service service) {
        return service.save(client)
                .onItem().transform(id -> URI.create("/service/" + id))
                .onItem().transform(uri -> Response.created(uri).build());
    }

    @DELETE
    @Path("/remove/service/{id}")
    public Uni<Response> delete(@PathParam Long id) {
        return Service.delete(client, id)
                .onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }

    @PUT
    @Path("/update/service/{id}/name/{name}")
    public Uni<Response> updateName(@PathParam Long id, @PathParam String name) {
        return Service.updateName(client, id, name)
                .onItem().transform(updated -> updated ? Status.NO_CONTENT : Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }
}
