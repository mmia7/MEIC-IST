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
import org.ie.entities.APilotDeveloper;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("apilotDeveloper")
public class APilotDeveloperResource {
    @Inject
    io.vertx.mutiny.mysqlclient.MySQLPool client;

    @GET
    @Path("/all")
    public Multi<APilotDeveloper> get() {
        return APilotDeveloper.findAll(client);
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getSingle(@PathParam Long id) {
        return APilotDeveloper.findById(client, id)
        .onItem().transform(apilotDeveloper -> apilotDeveloper != null ? Response.ok(apilotDeveloper) : Response.status(Status.NOT_FOUND)) 
        .onItem().transform(ResponseBuilder::build); 
    }

    @POST
    @Path("/add/APilotDeveloper")
    public Uni<Response> create(APilotDeveloper apilotDeveloper) {
        return apilotDeveloper.save(client)
        .onItem().transform(id -> URI.create("/apilotDeveloper/" + id))
        .onItem().transform(uri -> Response.created(uri).build());
    }

    @DELETE
    @Path("/remove/APilotDeveloper/{id}")
    public Uni<Response> delete(@PathParam Long id) {
        return APilotDeveloper.delete(client, id)
        .onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
        .onItem().transform(status -> Response.status(status).build());
    }
}