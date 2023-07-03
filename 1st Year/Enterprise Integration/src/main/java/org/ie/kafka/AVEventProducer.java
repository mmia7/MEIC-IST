package org.ie.kafka;

import io.quarkus.runtime.annotations.QuarkusMain;
import java.util.Properties;
import java.util.Random;
import java.sql.Timestamp;

import org.apache.kafka.clients.producer.KafkaProducer; 
import org.apache.kafka.clients.producer.ProducerRecord;

@QuarkusMain
public class AVEventProducer {

	final static String kafkaTopic = "av-event";
	static String brokerList = "ec2-44-201-119-255.compute-1.amazonaws.com:9092,ec2-3-83-193-41.compute-1.amazonaws.com:9092,ec2-18-206-223-228.compute-1.amazonaws.com:9092";

	static int throughput = 10;
	static String typeMessage = "JSON";
	static String filterprefix = "";

	public static void main(String[] args){
		createMessage();
	}

	
    public static void sendMessage(String msg){

        // TOPIC IN A KAFKA BROKER	
			Properties properties = new Properties();

			properties.put("bootstrap.servers",brokerList);
			properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer"); 
		    properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
            KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

            System.out.println("This is the message to send = " + msg);
            String seqkey = kafkaTopic + "_" + String.valueOf( ((Double) (Math.random() * 10)).intValue());
            System.out.println("Sending new message to Kafka topic=" + kafkaTopic + " with key=" + seqkey);	
            ProducerRecord<String, String> record = new ProducerRecord<>(kafkaTopic, seqkey, msg);		
            producer.send(record);
            System.out.println("AV Event Producer sent message...");
            producer.close();
	}

	private static void createMessage() {

		Timestamp mili = new Timestamp(System.currentTimeMillis());

		AVEvent avEvent = new AVEvent();

		avEvent.setTimeStamp(mili.toString());
		avEvent.setSeqkey(kafkaTopic + "_" + String.valueOf( ((Double) (Math.random() * 10)).intValue() ));
		avEvent.setAvId(kafkaTopic);

		avEvent.setSpeed( String.valueOf( ((Double) (Math.random() * 120)).intValue() ));
		avEvent.setBatteryLevel( String.valueOf( ((Double) (Math.random() * 100)).intValue() ));
		avEvent.setDriverTirenessLevel( String.valueOf( ((Double) (Math.random() * 100)).intValue() ));
		avEvent.setTractionWheelsLevel( String.valueOf( ((Double) (Math.random() * 100)).intValue() ));
		
        //verificar a location
		String[] locationOptions = {"38.73704375907657, -9.138709213484344", "38.73730834347317, -9.302641438338373" , "38.81178965928624, -9.093170965989835",  "Unknown"};
		avEvent.setLocation(locationOptions[new Random().nextInt(locationOptions.length)]);
		
		String[] EnvironmentalLightningOptions = {"N/A", "Bad" , "Sufficient" , "Good" , "Very Good" , "Excelent"};
		avEvent.setEnvironmentalLightning( EnvironmentalLightningOptions[new Random().nextInt(EnvironmentalLightningOptions.length)] );
		String[] RainConditionsOptions = {"N/A", "Light Rain" , "Medium Rain" , "Heavy Rain"};
		avEvent.setRainConditions( RainConditionsOptions[new Random().nextInt(RainConditionsOptions.length)]  );
		String[] FogConditionsOptions = {"N/A", "None" , "Light Fog" , "Medium Fog" , "Dense Fog"};
		avEvent.setFogConditions( FogConditionsOptions[new Random().nextInt(FogConditionsOptions.length)] );
		
		String[] TrafficLightOptions = {"Red", "Yellow" , "Green"};
		avEvent.setTrafficLight( TrafficLightOptions[new Random().nextInt(TrafficLightOptions.length)] );

		avEvent.setObstacleProximity( String.valueOf( ((Double) (Math.random() * 100)).intValue() ) );

		avEvent.setPedestrianProximity( String.valueOf( ((Double) (Math.random() * 100)).intValue() ) );

		avEvent.setAverageConsumptionLevel( String.valueOf( (((Double) (Math.random() * 100)).intValue()  * 0.05 / 100)  + 0.15 )); //in kwh

		sendMessage(avEvent.toString());

	}

}