package org.ie.entities;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class AV {

    public Long id;
    public String brand;
    public Long user_id;
    public Long service_id;
    public int price;

    public AV() {
    }

    public AV(Long id, String brand, Long user_id, Long service_id, Integer price) {
        this.id = id;
        this.brand=brand;
        this.service_id=service_id;
        this.user_id = user_id;
        this.price = price;
    }

    public Long getId(){
        return this.id;
    }
    public String getBrand(){
        return this.brand;
    }

    @JsonIgnore
    public Long getUserId(){
        return this.user_id;
    }
    
    @JsonIgnore
    public Long getServiceId(){
        return this.service_id;
    }
    public Integer getPrice(){
        return this.price;
    }
    
    private static AV from(Row row) {
        return new AV(row.getLong("id"),row.getString("brand"),row.getLong("user_id"),row.getLong("service_id"),row.getInteger("price"));
    }
    
    public static Multi<AV> findAll(MySQLPool client) {
        return client.query("SELECT id, brand, user_id, service_id, price FROM AV ORDER BY id ASC").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(AV::from);
    }
    
    public static Uni<AV> findById(MySQLPool client, Long id) {
        return client.preparedQuery("SELECT id, brand, user_id, service_id, price FROM AV WHERE id = ?").execute(Tuple.of(id)) 
                .onItem().transform(RowSet::iterator) 
                .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null); 
    }
    
    public Uni<Boolean> save(MySQLPool client) {
        return client.preparedQuery("INSERT INTO AV(id,brand,user_id,service_id,price) VALUES (?,?,?,?,?)").execute(Tuple.of(id,brand,user_id,service_id,price))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1 ); 
    }
    
    public static Uni<Boolean> delete(MySQLPool client, Long id) {
        return client.preparedQuery("DELETE FROM AV WHERE id = ?").execute(Tuple.of(id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1); 
    }

    public static Uni<Boolean> updatePrice(MySQLPool client, Long id, Integer price) {
        return client.preparedQuery("UPDATE AV SET price = ? WHERE id = ?").execute(Tuple.of(price,id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1 ); 
    }

    public static Uni<Boolean> updateService(MySQLPool client, Long id, Integer service_id) {
        return client.preparedQuery("UPDATE AV SET service_id = ? WHERE id = ?").execute(Tuple.of(service_id,id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1 ); 
    }


}
