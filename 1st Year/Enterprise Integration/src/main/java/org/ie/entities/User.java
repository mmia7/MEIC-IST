package org.ie.entities;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

public class User {
    public Long id;
    public String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

    private static User from(Row row) {
        return new User(row.getLong("id"), row.getString("name"));
    }

    public static Multi<User> findAll(MySQLPool client) {
        return client.query("SELECT id, name FROM user ORDER BY name ASC")
                .execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(User::from);
    }

    public static Uni<User> findById(MySQLPool client, Long id) {
        return client.preparedQuery("SELECT id, name FROM user WHERE id = ?")
                .execute(Tuple.of(id))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public Uni<Boolean> save(MySQLPool client) {
        return client.preparedQuery("INSERT INTO user(name) VALUES (?)")
                .execute(Tuple.of(name))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public static Uni<Boolean> delete(MySQLPool client, Long id) {
        return client.preparedQuery("DELETE FROM user WHERE id = ?")
                .execute(Tuple.of(id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public static Uni<Boolean> updateName(MySQLPool client, Long id, String name) {
        return client.preparedQuery("UPDATE user SET name = ? WHERE id = ?")
                .execute(Tuple.of(name, id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }
}
