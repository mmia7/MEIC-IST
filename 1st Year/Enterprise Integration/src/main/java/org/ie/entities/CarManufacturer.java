package org.ie.entities;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class CarManufacturer {
    public String brand;
    static String brokerList = "ec2-44-201-119-255.compute-1.amazonaws.com:9092,ec2-3-83-193-41.compute-1.amazonaws.com:9092,ec2-18-206-223-228.compute-1.amazonaws.com:9092";


    public CarManufacturer() {
    }

    public CarManufacturer(String brand) {
        this.brand = brand;
        
    }

    public String getBrand() {
		return this.brand;
	}

    public void createTopic(){

		// User_event TOPIC IN A KAFKA BROKER	
			Properties properties = new Properties();

			properties.put("bootstrap.servers",brokerList);
			properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer"); 
		    properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
            
			try (AdminClient clientkafka = AdminClient.create(properties)) 
			{
					CreateTopicsResult result = clientkafka.createTopics(Arrays.asList(
						new NewTopic("Manufacturer_Catalog_"+ this.brand, 2, (short) 3)));

				try { 
					result.all().get(); 
				}
				catch ( ExecutionException | InterruptedException e ){
					 throw new IllegalStateException(e); 
				}	
			}			
	}

    private static CarManufacturer from(Row row) {
        return new CarManufacturer(row.getString("brand"));
    }

    public static Multi<CarManufacturer> findAll(MySQLPool client) {
        return client.query("SELECT brand FROM carManufacturer ORDER BY brand ASC")
                .execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(CarManufacturer::from);
    }

    public static Uni<CarManufacturer> findByName(MySQLPool client, String brand) {
        return client.preparedQuery("SELECT brand FROM carManufacturer WHERE brand = ?")
                .execute(Tuple.of(brand))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public Uni<Boolean> save(MySQLPool client) {
        createTopic();
        
        return client.preparedQuery("INSERT INTO carManufacturer(brand) VALUES (?)")
                .execute(Tuple.of(brand))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public static Uni<Boolean> delete(MySQLPool client, String brand) {
        return client.preparedQuery("DELETE FROM carManufacturer WHERE brand = ?")
                .execute(Tuple.of(brand))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }
}
