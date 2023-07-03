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

public class APilot {
    public Long id;
    public Long developer_id;
    public Integer version;
    static String brokerList = "ec2-44-201-119-255.compute-1.amazonaws.com:9092,ec2-3-83-193-41.compute-1.amazonaws.com:9092,ec2-18-206-223-228.compute-1.amazonaws.com:9092";


    public APilot() {
    }

    public APilot(Long id, Long developer_id, Integer version) {
        this.id = id;
        this.developer_id = developer_id;
        this.version = version;
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
						new NewTopic("APilot_Catalog_"+ String.valueOf(this.id), 2, (short) 3)));

				try { 
					result.all().get(); 
				}
				catch ( ExecutionException | InterruptedException e ){
					 throw new IllegalStateException(e); 
				}	
			}			
	}

    private static APilot from(Row row) {
        return new APilot(row.getLong("id"), row.getLong("developer_id"), row.getInteger("version"));
    }

    public static Multi<APilot> findAll(MySQLPool client) {
        return client.query("SELECT id, developer_id, version FROM APilot")
                .execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(APilot::from);
    }

    public static Uni<APilot> findById(MySQLPool client, Long id) {
        return client.preparedQuery("SELECT id, developer_id, version FROM APilot WHERE id = ?")
                .execute(Tuple.of(id))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

	public Uni<Boolean> save(MySQLPool client) {
        createTopic();
		return client.preparedQuery("INSERT INTO APilot(id, developer_id, version) VALUES (?, ?, ?)").execute(Tuple.of(id, developer_id, version)).onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
	}
	
    public static Uni<Boolean> delete(MySQLPool client, Long id) {
        return client.preparedQuery("DELETE FROM APilot WHERE id = ?")
                .execute(Tuple.of(id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public static Uni<Boolean> updateVersion(MySQLPool client, Long id, Integer version) {
        return client.preparedQuery("UPDATE APilot SET version = ? WHERE id = ?")
                .execute(Tuple.of(version, id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }
}
