package org.ie.kafka;

public class APilot {
	
	private String timeStamp;
	private String av_id;
	private String speed;
	private String applyBrakes; // yes or no
	private String batteryLevel;
	private String chargeCar; // yes or no
	private String driverTirenessLevel; //0 to 5
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
	
	public APilot() {
		super();
		
	}
	
	public APilot(String timeStamp, String av_id, String speed, String applyBrakes, String batteryLevel, String chargeCar, String driverTirenessLevel, 
			String takeRest, String location, String environmentalLightning, String spaceVisualization, String rainConditions, 
			String fogConditions, String weatherStatus, String tractionWheelsLevel, String trafficLight, String obstacleProximity,
			String pedestrianProximity, String averageConsumptionLevel, String avStatus) {
				
		this.timeStamp = timeStamp;
		this.av_id = av_id;
		this.speed = speed;
		this.applyBrakes = applyBrakes;
		this.batteryLevel = batteryLevel;
		this.chargeCar = chargeCar;
		this.driverTirenessLevel = driverTirenessLevel;
		this.takeRest = takeRest;
		this.location = location;
		this.environmentalLightning = environmentalLightning;
		this.spaceVisualization = spaceVisualization;
		this.rainConditions = rainConditions;
		this.fogConditions = fogConditions;
		this.weatherStatus = weatherStatus;
		this.tractionWheelsLevel = tractionWheelsLevel;
		this.trafficLight = trafficLight;
		this.obstacleProximity = obstacleProximity;
		this.pedestrianProximity = pedestrianProximity;
		this.averageConsumptionLevel = averageConsumptionLevel;
		this.avStatus = avStatus;

	}


	@Override
	public String toString() {
		return "APilot ["+"TimeStamp=" + timeStamp + "; av_id=" + av_id + "; speed=" + speed + "; applyBrakes=" + applyBrakes + "; batteryLevel=" + batteryLevel + "; chargeCar=" + chargeCar + "; driverTirenessLevel="
				+ driverTirenessLevel + "; takeRest=" + takeRest + "; location=" + location + "; environmentalLightning=" + environmentalLightning + "; spaceVisualization=" + spaceVisualization
				+ "; rainConditions=" + rainConditions + "; fogConditions=" + fogConditions + "; weatherStatus=" + weatherStatus + "; tractionWheelsLevel=" + tractionWheelsLevel 
				+ "; trafficLight=" + trafficLight + "; obstacleProximity=" + obstacleProximity + "; pedestrianProximity=" + pedestrianProximity 
				+ "; averageConsumptionLevel =" + averageConsumptionLevel + "; avStatus=" + avStatus + "]";
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getAvId() {
		return av_id;
	}

	public void setAvId(String av_id) {
		this.av_id = av_id;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getApplyBrakes() {
		return applyBrakes;
	}

	public void setApplyBrakes(String applyBrakes) {
		this.applyBrakes = applyBrakes;
	}

	public String getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(String batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public String getChargeCar() {
		return chargeCar;
	}

	public void setChargeCar(String chargeCar) {
		this.chargeCar = chargeCar;
	}

	public String getDriverTirenessLevel() {
		return driverTirenessLevel;
	}

	public void setDriverTirenessLevel(String driverTirenessLevel) {
		this.driverTirenessLevel = driverTirenessLevel;
	}

	public String getTakeRest() {
		return takeRest;
	}

	public void setTakeRest(String takeRest) {
		this.takeRest = takeRest;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEnvironmentalLightning() {
		return environmentalLightning;
	}

	public void setEnvironmentalLightning(String environmentalLightning) {
		this.environmentalLightning = environmentalLightning;
	}

	public String getSpaceVisualization() {
		return spaceVisualization;
	}

	public void setSpaceVisualization(String spaceVisualization) {
		this.spaceVisualization = spaceVisualization;
	}

	public String getRainConditions() {
		return rainConditions;
	}

	public void setRainConditions(String rainConditions) {
		this.rainConditions = rainConditions;
	}

	public String getFogConditions() {
		return fogConditions;
	}

	public void setFogConditions(String fogConditions) {
		this.fogConditions = fogConditions;
	}

	public String getWeatherStatus() {
		return weatherStatus;
	}

	public void setWeatherStatus(String weatherStatus) {
		this.weatherStatus = weatherStatus;
	}

	public String getTractionWheelsLevel() {
		return tractionWheelsLevel;
	}

	public void setTractionWheelsLevel(String tractionWheelsLevel) {
		this.tractionWheelsLevel = tractionWheelsLevel;
	}

	public String getAvStatus() {
		return avStatus;
	}

	public void setAvStatus(String avStatus) {
		this.avStatus = avStatus;
	}
	
	public String getTrafficLight() {
		return trafficLight;
	}
	public void setTrafficLight(String trafficLight) {
		this.trafficLight = trafficLight;
	}
	public String getObstacleProximity() {
		return obstacleProximity;
	}
	public void setObstacleProximity(String obstacleProximity) {
		this.obstacleProximity = obstacleProximity;
	}
	public String getPedestrianProximity() {
		return pedestrianProximity;
	}
	public void setPedestrianProximity(String pedestrianProximity) {
		this.pedestrianProximity = pedestrianProximity;
	}
	public String getAverageConsumptionLevel() {
		return averageConsumptionLevel;
	}
	public void setAverageConsumptionLevel(String averageConsumptionLevel) {
		this.averageConsumptionLevel = averageConsumptionLevel;
	}	
	
	
	
}