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
import org.ie.entities.User;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("user")
public class UserResource {
    @Inject
    io.vertx.mutiny.mysqlclient.MySQLPool client;

    @GET
    @Path("/all")
    public Multi<User> get() {
        return User.findAll(client);
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSingle(@PathParam Long id) {
        return User.findById(client, id)
                .onItem().transform(user -> user != null ? Response.ok(user) : Response.status(Status.NOT_FOUND))
                .onItem().transform(ResponseBuilder::build);
    }

    @POST
    @Path("/add/user")
    public Uni<Response> create(User user) {
        return user.save(client)
                .onItem().transform(id -> URI.create("/user/" + id))
                .onItem().transform(uri -> Response.created(uri).build());
    }

    @DELETE
    @Path("/remove/user/{id}")
    public Uni<Response> delete(@PathParam Long id) {
        return User.delete(client, id)
                .onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }

    @PUT
    @Path("/update/user/{id}/name/{name}")
    public Uni<Response> updateName(@PathParam Long id, @PathParam String name) {
        return User.updateName(client, id, name)
                .onItem().transform(updated -> updated ? Status.NO_CONTENT : Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }
}
