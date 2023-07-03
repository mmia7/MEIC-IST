package org.ie.entities;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

public class APilotDeveloper {
    public Long id;

    public APilotDeveloper() {
    }

    public APilotDeveloper(Long id) {
        this.id = id;
    }

    private static APilotDeveloper from(Row row) {
        return new APilotDeveloper(row.getLong("id"));
    }

    public static Multi<APilotDeveloper> findAll(MySQLPool client) {
        return client.query("SELECT id FROM apilotDeveloper")
                .execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(APilotDeveloper::from);
    }

    public static Uni<APilotDeveloper> findById(MySQLPool client, Long id) {
        return client.preparedQuery("SELECT id FROM apilotDeveloper WHERE id = ?")
                .execute(Tuple.of(id))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public static Uni<Boolean> delete(MySQLPool client, Long id) {
        return client.preparedQuery("DELETE FROM apilotDeveloper WHERE id = ?")
                .execute(Tuple.of(id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public Uni<Boolean> save(MySQLPool client) {
		return client.preparedQuery("INSERT INTO apilotDeveloper(id) VALUES (?)").execute(Tuple.of(id)).onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
	}
}
