package org.ie.services;

import java.net.URI;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.jboss.logging.annotations.Param;
import org.ie.entities.APilot;
import io.smallrye.mutiny.Uni;

@Path("APilotService")
public class APilotService {
	
	@Inject
	io.vertx.mutiny.mysqlclient.MySQLPool client;
	
	@POST
	@Path("/add/APilot")
	public Uni<Response> create(APilot aPilot) {
        return aPilot.save(client)
				.onItem().transform(id -> URI.create("/APilotService/add/APilot/" + id))
				.onItem().transform(uri -> Response.created(uri).build())
				.onFailure().recoverWithUni(Uni.createFrom().item(() -> Response.status(Response.Status.NOT_ACCEPTABLE)
				.entity("Error invalid data.\nAPilot already exists OR invalid Brand OR repeated Version.\n").build()));
	}
	
	@DELETE
	@Path("/remove/APilot/{id}")
	public Uni<Response> delete(@Param Long id) {
		return APilot.delete(client, id)
				.onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
				.onItem().transform(status -> Response.status(status).build());
	}

	@PUT
	@Path("/update/APilot/{id}/version/{version}")
	public Uni<Response> updateVersion(@Param Long id , @Param Integer version) {
		return APilot.updateVersion(client, id , version)
				.onItem().transform(updated -> updated ? Status.NO_CONTENT : Status.NOT_FOUND)
				.onItem().transform(status -> Response.status(status).build());
	}
}