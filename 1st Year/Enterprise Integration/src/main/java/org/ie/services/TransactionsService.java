package org.ie.services;

import java.net.URI;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.ie.entities.Transactions;
import io.smallrye.mutiny.Uni;

@Path("transactionsService")
public class TransactionsService {
	
	@Inject
	io.vertx.mutiny.mysqlclient.MySQLPool client;

    private boolean isTransactionValid(Transactions transaction) {
		return transaction.getId() >= 0  && transaction.getUserId() >= 0 
				&& transaction.getAvId() >= 0 && transaction.getAPilotId() >= 0 ;	
	}
	
	@POST
	@Path("/buy/AV")
	public Uni<Response> buy(Transactions transaction) {
		
		if(!isTransactionValid(transaction)) {
			String msg = "Error purchasing AV\n";
			return Uni.createFrom().item(() -> Response.status(Response.Status.NOT_ACCEPTABLE).entity(msg).build());
		}
		
		return transaction.buyAV(client)
				.onItem().transform(id -> URI.create("/transaction_service/buy/AV/" + id))
				.onItem().transform(uri -> Response.created(uri).build())
				.onFailure().recoverWithUni(Uni.createFrom().item(() -> Response.status(Response.Status.NOT_ACCEPTABLE)
				.entity("Error purchasing.\nTransaction id already exists OR User id already exists OR AV id already exists OR APilot id already exists\n").build()));
	}
	
	@PUT
	@Path("/sell/AV/{id}")
	public Uni<Response> sellAV(@PathParam Long id) {
		return Transactions.sellAV(client, id)
				.onItem().transform(updated -> updated ? Status.NO_CONTENT : Status.NOT_FOUND)
				.onItem().transform(status -> Response.status(status).build());
	}
	
	@PUT
	@Path("/select/APilot/{apilotId}")
	public Uni<Response> selectAPilot(@PathParam Long id , @PathParam Long apilotId) {
		return Transactions.selectAPilot(client,id, apilotId)
				.onItem().transform(updated -> updated ? Status.NO_CONTENT : Status.NOT_FOUND)
				.onItem().transform(status -> Response.status(status).build());
	}
	
	@PUT
	@Path("/unselect/APilot/{id}")
	public Uni<Response> unselectAPilot(@PathParam Long id) {
		return Transactions.unselectAPilot(client, id)
				.onItem().transform(updated -> updated ? Status.NO_CONTENT : Status.NOT_FOUND)
				.onItem().transform(status -> Response.status(status).build());
	}
}