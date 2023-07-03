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
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.ie.entities.AV;


@Path("av")
public class AVResource {
    @Inject
    io.vertx.mutiny.mysqlclient.MySQLPool client;
   
    @GET
    @Path("/all")
    public Multi<AV> get() {
        return AV.findAll(client);
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getSingle(@PathParam Long id) {
        return AV.findById(client, id)
        .onItem().transform(AV -> AV != null ? Response.ok(AV) : Response.status(Status.NOT_FOUND)) 
        .onItem().transform(ResponseBuilder::build); 
    }

    @POST
    @Path("/add/AV")
    public Uni<Response> create(AV av) {
        return av.save(client)
        .onItem().transform(id -> URI.create("/av/" + id))
        .onItem().transform(uri -> Response.created(uri).build());
    }

    @DELETE
    @Path("/remove/AV/{id}")
    public Uni<Response> delete(@PathParam Long id) {
        return AV.delete(client, id)
        .onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
        .onItem().transform(status -> Response.status(status).build());
    }
    @PUT
    @Path("/update/AV/{id}/price/{price}")
    public Uni<Response> update(@PathParam Long id , @PathParam Integer price) {
        return AV.updatePrice(client, id , price)
        .onItem().transform(updated -> updated ? Status.NO_CONTENT : Status.NOT_FOUND)
        .onItem().transform(status -> Response.status(status).build());
    } 
    @PUT
    @Path("/update/AV/{id}/service/{service_id}")
    public Uni<Response> updateService(@PathParam Long id , @PathParam Integer service_id) {
        return AV.updatePrice(client, id , service_id)
        .onItem().transform(updated -> updated ? Status.NO_CONTENT : Status.NOT_FOUND)
        .onItem().transform(status -> Response.status(status).build());
    } 
}
