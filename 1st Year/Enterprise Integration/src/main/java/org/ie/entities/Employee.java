package org.ie.entities;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

public class Employee {

    public Long id;
    public long user_id;

    public Employee() {
    }

    public Employee(Long id, Long user_id) {
        this.id = id;
        this.user_id = user_id;
    }
    
    private static Employee from(Row row) {
        return new Employee(row.getLong("id"),row.getLong("user_id"));
    }
    
    public static Multi<Employee> findAll(MySQLPool client) {
        return client.query("SELECT id, user_id FROM employee ORDER BY id ASC").execute()
        .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
        .onItem().transform(Employee::from);
    }
    
    public static Uni<Employee> findById(MySQLPool client, Long id) {return client.preparedQuery("SELECT id,user_id FROM employee WHERE id = ?").execute(Tuple.of(id)) 
        .onItem().transform(RowSet::iterator) 
        .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null); 
    }
    
    public Uni<Boolean> save(MySQLPool client) {
        return client.preparedQuery("INSERT INTO employee(id,user_id) VALUES (?,?)").execute(Tuple.of(id,user_id))
        .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1 ); 
    }
    
    public static Uni<Boolean> delete(MySQLPool client, Long id) {
        return client.preparedQuery("DELETE FROM employee WHERE id = ?").execute(Tuple.of(id))
        .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1); 
    }
}
