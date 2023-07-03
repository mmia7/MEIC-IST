package org.ie.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ApilotProducer {

	private final static String kafkaTopic = "apilot";
	static String brokerList = "ec2-44-201-119-255.compute-1.amazonaws.com:9092,ec2-3-83-193-41.compute-1.amazonaws.com:9092,ec2-18-206-223-228.compute-1.amazonaws.com:9092";

	static int throughput = 10;
	static String typeMessage = "JSON";
	static String filterprefix = "";

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
            System.out.println("APilot Producer sent message...");
            producer.close();
	}
	
	public static void createMessage(AVEvent avEvent) {
				
		APilot apilot = new APilot();
		
		apilot.setTimeStamp(avEvent.getTimeStamp());
		apilot.setAvId(avEvent.getAvId());
		apilot.setSpeed(avEvent.getSpeed());
        apilot.setTrafficLight(avEvent.getTrafficLight());
        apilot.setObstacleProximity(avEvent.getObstacleProximity());
        apilot.setPedestrianProximity(avEvent.getPedestrianProximity());
        apilot.setAverageConsumptionLevel(avEvent.getAverageConsumptionLevel());
		apilot.setBatteryLevel(avEvent.getBatteryLevel());
		apilot.setDriverTirenessLevel(avEvent.getDriverTirenessLevel());
		apilot.setLocation(avEvent.getLocation());
		apilot.setEnvironmentalLightning(avEvent.getEnvironmentalLightning());
		apilot.setRainConditions(avEvent.getRainConditions());
		apilot.setFogConditions(avEvent.getFogConditions());
		apilot.setTractionWheelsLevel(avEvent.getTractionWheelsLevel());

		sendMessage(apilot.toString());
	}

}