package org.ie.services;

import java.net.URI;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.ie.entities.User;

import org.jboss.logging.annotations.Param;
import io.smallrye.mutiny.Uni;

@Path("subscriptionService")
public class SubscriptionService {
	
	@Inject
	io.vertx.mutiny.mysqlclient.MySQLPool client;

	private boolean isUserValid(User user) {
		return user.getId() >= 0 && user.getName().length() > 0 ;		
	}
	
	@POST
	@Path("/subscribe/user")
	public Uni<Response> create(User user) {
		
		if(!isUserValid(user)) {
			String msg = "Error subscribing user\n";
			return Uni.createFrom().item(() -> Response.status(Response.Status.NOT_ACCEPTABLE).entity(msg).build());
		}
		
		
		return user.save(client)
				.onItem().transform(id -> URI.create("/subscriptionService/subscribe/user/" + id))
				.onItem().transform(uri -> Response.created(uri).build())
				.onFailure().recoverWithUni(Uni.createFrom().item(() -> Response.status(Response.Status.NOT_ACCEPTABLE)
				.entity("User already exists.\n").build()));
	}
	
	
	@DELETE
	@Path("/unsubscribe/user/{id}")
	public Uni<Response> delete(@Param Long id) {
		return User.delete(client, id)
				.onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
				.onItem().transform(status -> Response.status(status).build());
	}
}