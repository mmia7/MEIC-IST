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

import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.ie.entities.APilot;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("apilot")
public class APilotResource {
    @Inject
    io.vertx.mutiny.mysqlclient.MySQLPool client;

    @GET
    @Path("/all")
    public Multi<APilot> get() {
        return APilot.findAll(client);
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getSingle(@PathParam Long id) {
        return APilot.findById(client, id)
        .onItem().transform(APilot -> APilot != null ? Response.ok(APilot) : Response.status(Status.NOT_FOUND)) 
        .onItem().transform(ResponseBuilder::build); 
    }

    @POST
    @Path("/add/APilot")
    public Uni<Response> create(APilot apilot) {
        return apilot.save(client)
        .onItem().transform(id -> URI.create("/apilot/" + id))
        .onItem().transform(uri -> Response.created(uri).build());
    }

    @DELETE
    @Path("/remove/APilot/{id}")
    public Uni<Response> delete(@PathParam Long id) {
        return APilot.delete(client, id)
        .onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
        .onItem().transform(status -> Response.status(status).build());
    }
}