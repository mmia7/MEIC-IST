package org.ie.kafka;

import io.smallrye.reactive.messaging.kafka.Record;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

public class DecisionConsumer {

    private String timeStamp;
	private String av_id;
	private String speed;
	private String applyBrakes; // yes or no
	private String batteryLevel;
	private String chargeCar; // yes or no
	private String driverTirenessLevel; //0 to 100
	private String takeRest;  // yes or no
	private String location;
	private String environmentalLightning; 
	private String spaceVisualization; // good or bad 
	private String rainConditions;    
	private String fogConditions;
	private String weatherStatus; // good or bad 
	private String tractionWheelsLevel;
	private String trafficLight;
 	private String obstacleProximity;
	private String pedestrianProximity;
 	private String averageConsumptionLevel;
	private String avStatus; //slow down or continue

	final Logger logger = Logger.getLogger(DecisionConsumer.class);

	@Incoming("apilot")
	public void receiveAPilot(Record<String, String> record) {

		logger.infof("Consumed an APilot: Id = " + record.key() + " Value = " + record.value() + "\n"); 
        
        apilotFunctionality(record.value());

        APilot apilot = new APilot(timeStamp, av_id, speed,applyBrakes, batteryLevel,chargeCar, driverTirenessLevel,takeRest, location, environmentalLightning,
                                spaceVisualization, rainConditions, fogConditions,weatherStatus, tractionWheelsLevel, trafficLight,obstacleProximity,pedestrianProximity,averageConsumptionLevel,avStatus); 
		
		DecisionProducer.createMessage(apilot);

		System.out.println ("----------------------------------------\n");
	}

	private void apilotFunctionality(String msg) {

		String[] msgArray = msg.replace("\"APilot\" [", "").replace("]", "").split(";");

		timeStamp = msgArray[0].split("=")[1];
		av_id = msgArray[1].split("=")[1];
		speed = msgArray[2].split("=")[1];
		batteryLevel = msgArray[4].split("=")[1];
		driverTirenessLevel = msgArray[6].split("=")[1];
		location = msgArray[8].split("=")[1];
		environmentalLightning = msgArray[10].split("=")[1];
		rainConditions = msgArray[12].split("=")[1];
		fogConditions = msgArray[13].split("=")[1];
		tractionWheelsLevel = msgArray[15].split("=")[1];
        trafficLight = msgArray[16].split("=")[1];
 	    obstacleProximity = msgArray[17].split("=")[1];
        pedestrianProximity = msgArray[18].split("=")[1];
        averageConsumptionLevel = msgArray[19].split("=")[1];
		avStatus = msgArray[16].split("=")[1];

		if(trafficLight.equals("Red") || Integer.parseInt(averageConsumptionLevel) > 0.5) {
			applyBrakes = "Yes";
	    } 
        else {
			applyBrakes = "No";
		}

		if(Integer.parseInt(batteryLevel) < 10) {
			chargeCar = "Yes";
		}
         else {
			chargeCar ="No";
		}

		if(Integer.parseInt(driverTirenessLevel) > 80) {
			takeRest = "Yes";
		} 
        else {
			takeRest ="No";
		}

		if(environmentalLightning.equals("N/A") || environmentalLightning.equals("Bad")) {
			spaceVisualization = "Bad";
		} 
        else {
			spaceVisualization = "Good";
		}

		if(rainConditions.equals("Heavy Rain") || fogConditions.equals("Dense Fog")) {
			weatherStatus = "Bad";
		} 
        else {
			weatherStatus = "Good";
		}

		if(Integer.parseInt(speed) > 50 || chargeCar.equals("Yes") || takeRest.equals("Yes") || location.equals("Unknown") || 
			spaceVisualization.equals("Bad") ||weatherStatus.equals("Bad") || Integer.parseInt(tractionWheelsLevel) < 20 || 
			trafficLight.equals("Red") || Integer.parseInt(obstacleProximity) < 10 || Integer.parseInt(pedestrianProximity) < 10) {

			avStatus = "Slow Down";
		} 
		else {
			avStatus = "Continue";
		}
	}
}