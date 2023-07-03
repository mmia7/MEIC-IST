package org.ie.entities;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Transactions {
	
	public Long id;
	public Long user_id;
	public Long av_id;
	public Long apilot_id;
	public String description;
	
	public Transactions() {
	}
	
	public Transactions(Long id, Long user_id, Long av_id, Long apilot_id,String description) {
		this.id = id;
		this.user_id = user_id;
		this.av_id = av_id;
		this.apilot_id = apilot_id;
		this.description = description;
	}
	
	public Long getId() {
		return this.id;
	}

	@JsonIgnore
	public Long getUserId() {
		return this.user_id;
	}

	@JsonIgnore
	public Long getAvId() {
		return this.av_id;
	}

	@JsonIgnore
	public Long getAPilotId() {
		return this.apilot_id;
	}

	public String getDescription(){
		return this.description;
	}

    private static Transactions from(Row row) {
		return new Transactions(row.getLong("id"), row.getLong("user_id"), row.getLong("av_id"), row.getLong("apilot_id"), row.getString("description"));
	}
	
	public static Multi<Transactions> findAll(MySQLPool client) {
		return client.query("SELECT id, user_id, av_id, apilot_id, description FROM transactions")
				.execute()
				.onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
				.onItem().transform(Transactions::from);
	}
	
	public static Uni<Transactions> findById(MySQLPool client, Long id) {
		return client.preparedQuery("SELECT id, user_id, av_id, apilot_id, description FROM transactions WHERE id = ?")
				.execute(Tuple.of(id))
				.onItem().transform(RowSet::iterator)
				.onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
	}

	public static Uni<Boolean> delete(MySQLPool client, Long id) {
		return client.preparedQuery("DELETE FROM transactions WHERE id = ?").execute(Tuple.of(id))
				.onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
	}
	
	public Uni<Boolean> buyAV(MySQLPool client) {

		return client.preparedQuery("INSERT INTO transactions(id, user_id, av_id, apilot_id,description) VALUES (?, ?, ?, ?,'buy')").execute(Tuple.of(id, user_id, av_id, apilot_id,description))
				.onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
	}

	public static Uni<Boolean> sellAV(MySQLPool client, Long id) {

		return client.preparedQuery("UPDATE transactions SET user_id = NULL, description = 'sell' WHERE id = ?").execute(Tuple.of(id))
				.onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1 );
	}

	public static Uni<Boolean> selectAPilot(MySQLPool client, Long id, Long apilot_id) {

		return client.preparedQuery("UPDATE transactions SET apilot_id = ?, description = 'select' WHERE id = ?").execute(Tuple.of(apilot_id,id))
				.onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1 );
	}
	
	public static Uni<Boolean> unselectAPilot(MySQLPool client, Long id) {

		return client.preparedQuery("UPDATE transactions SET apilot_id = NULL, description = 'unselect' WHERE id = ?").execute(Tuple.of(id))
				.onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1 );
	}
}