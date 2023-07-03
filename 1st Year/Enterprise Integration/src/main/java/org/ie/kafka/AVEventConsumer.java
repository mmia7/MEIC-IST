package org.ie.kafka;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import io.smallrye.reactive.messaging.kafka.Record;

public class AVEventConsumer {

	private String timeStamp;
	private String av_Id;
	private String speed;
	private String batteryLevel;
	private String driverTirenessLevel;
	private String location;
	private String environmentalLightning;
	private String rainConditions;
	private String fogConditions;
	private String tractionWheelsLevel;
	private String trafficLight;
	private String obstacleProximity;
	private String pedestrianProximity;
	private String averageConsumptionLevel;

	final Logger logger = Logger.getLogger(AVEventConsumer.class);

	@Incoming("av-event")
	public void consumeAv(Record<Integer, String> record) {
		
		logger.infof("Consumed AV_Event: Id = " +  record.key() + " Value =  " + record.value() + "\n");
		
		mediationToAPilot(record.value());

		AVEvent avEvent = new AVEvent(timeStamp,record.key().toString(), av_Id, speed, batteryLevel, driverTirenessLevel, location, environmentalLightning, rainConditions, fogConditions, 
                                    tractionWheelsLevel, trafficLight,obstacleProximity,pedestrianProximity,averageConsumptionLevel); 
		
		ApilotProducer.createMessage(avEvent);
		
		System.out.println ("----------------------------------------\n");
	}
		
	
	private void mediationToAPilot(String msg) {

		String[] msgArray = msg.replace("\"AvEvent\" [", "").replace("]", "").split(";");

		timeStamp = msgArray[0].split("=")[1];
		av_Id = msgArray[2].split("=")[1];
		speed = msgArray[3].split("=")[1];
		batteryLevel = msgArray[4].split("=")[1];
		driverTirenessLevel = msgArray[5].split("=")[1];
		location = msgArray[6].split("=")[1];
		environmentalLightning = msgArray[7].split("=")[1];
		rainConditions = msgArray[8].split("=")[1];
		fogConditions = msgArray[9].split("=")[1];
		tractionWheelsLevel = msgArray[10].split("=")[1];
        trafficLight = msgArray[11].split("=")[1];
        obstacleProximity = msgArray[12].split("=")[1];
        pedestrianProximity = msgArray[13].split("=")[1];
        averageConsumptionLevel = msgArray[14].split("=")[1];


	}
	
	
}