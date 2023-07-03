package org.ie.entities;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Service {
    public Long id;
    public String provider_id;
    public String name;


    public Service() {
    }

    public Service(Long id,String provider_id,String name) {
        this.id = id;
        this.provider_id= provider_id;
        this.name = name;
    }

    public long getId() {
		return id;
	}

    @JsonIgnore
    public String getProvider(){
        return this.provider_id;
    }

	public String getName() {
		return name;
	}

    private static Service from(Row row) {
        return new Service(row.getLong("id"),row.getString("provider_id"), row.getString("name"));
    }

    public static Multi<Service> findAll(MySQLPool client) {
        return client.query("SELECT id, provider_id, name FROM service ORDER BY name ASC")
                .execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Service::from);
    }

    public static Uni<Service> findById(MySQLPool client, Long id) {
        return client.preparedQuery("SELECT id, provider_id, name FROM service WHERE id = ?")
                .execute(Tuple.of(id))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public Uni<Boolean> save(MySQLPool client) {
        return client.preparedQuery("INSERT INTO service(id,provider_id,name) VALUES (?,?,?)")
                .execute(Tuple.of(id,provider_id,name))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public static Uni<Boolean> delete(MySQLPool client, Long id) {
        return client.preparedQuery("DELETE FROM service WHERE id = ?")
                .execute(Tuple.of(id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public static Uni<Boolean> updateName(MySQLPool client, Long id, String name) {
        return client.preparedQuery("UPDATE service SET name = ? WHERE id = ?")
                .execute(Tuple.of(name, id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }
}
