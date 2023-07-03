package org.ie.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;

import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.ie.entities.Transactions;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("transactions")
public class TransactionsResource {
    @Inject
    io.vertx.mutiny.mysqlclient.MySQLPool client;

    @GET
    @Path("/all")
    public Multi<Transactions> get() {
        return Transactions.findAll(client);
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSingle(@PathParam Long id) {
        return Transactions.findById(client, id)
                .onItem().transform(transactions -> transactions != null ? Response.ok(transactions) : Response.status(Status.NOT_FOUND))
                .onItem().transform(ResponseBuilder::build);
    }

    @DELETE
    @Path("/remove/transaction/{id}")
    public Uni<Response> delete(@PathParam Long id) {
        return Transactions.delete(client, id)
                .onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }

}
