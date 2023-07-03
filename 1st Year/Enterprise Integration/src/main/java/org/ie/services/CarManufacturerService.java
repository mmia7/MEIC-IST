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

import org.ie.entities.AV;
import io.smallrye.mutiny.Uni;

@Path("carManufacturerService")
public class CarManufacturerService {
	
	@Inject
	io.vertx.mutiny.mysqlclient.MySQLPool client;

	private boolean isAvValid(AV av) {
		return av.getId() >= 0 && av.getBrand().length() > 0 && av.getUserId() >=0 && av.getServiceId() >=0 && av.getPrice() >=0;
	}

	@POST
	@Path("/add/AV")
	public Uni<Response> create(AV av) {

		if(!isAvValid(av)) {
			String msg = "Error subscribing user\n";
			return Uni.createFrom().item(() -> Response.status(Response.Status.NOT_ACCEPTABLE).entity(msg).build());
		}
		return av.save(client)
				.onItem().transform(id -> URI.create("/carManufacturerService/add/AV/" + id))
				.onItem().transform(uri -> Response.created(uri).build())
				.onFailure().recoverWithUni(Uni.createFrom().item(() -> Response.status(Response.Status.NOT_ACCEPTABLE)
				.entity("Error invalid data.\nOr AV already exists OR invalid Brand name invalid Service id OR invalid Price.\n").build()));
	}
	
	@DELETE
	@Path("remove/AV/{id}")
	public Uni<Response> delete(@Param Long id) {
		return AV.delete(client, id)
				.onItem().transform(deleted -> deleted ? Status.NO_CONTENT : Status.NOT_FOUND)
				.onItem().transform(status -> Response.status(status).build());
	}

	@PUT
	@Path("/update/av/{id}/price/{price}")
	public Uni<Response> updatePrice(@Param Long id , @Param Integer price) {
		return AV.updatePrice(client, id , price)
				.onItem().transform(updated -> updated ? Status.NO_CONTENT : Status.NOT_FOUND)
				.onItem().transform(status -> Response.status(status).build());
	}

}