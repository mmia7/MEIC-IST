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
import org.ie.entities.CarManufacturer;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("carManufacturer")
public class CarManufacturerResource {
    
    @Inject
    io.vertx.mutiny.mysqlclient.MySQLPool client;
    
    @GET
    @Path("/all")
    public Multi<CarManufacturer> get() {
        return CarManufacturer.findAll(client);
    }

    @GET
    @Path("{brand}")
    public Uni<Response> getSingle(@PathParam String brand) {
        return CarManufacturer.findByName(client, brand)
                .onItem().transform(carManufacturer -> carManufacturer != null ? Response.ok(carManufacturer) : Response.status(Status.NOT_FOUND))
                .onItem().transform(ResponseBuilder::build);
    }

    @POST
    @Path("/add/brand")
    public Uni<Response> create(CarManufacturer carManufacturer) {
        return carManufacturer.save(client)
                .onItem().transform(brand -> URI.create("/carManufacturer/" + brand))
                .onItem().transform(uri -> Response.created(uri).build());
    }

    @DELETE
    @Path("/remove/brand/{brand}")
    public Uni<Response> delete(@PathParam String brand) {
        return CarManufacturer.delete(client, brand)
                .onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }
}
