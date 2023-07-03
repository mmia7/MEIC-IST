
package org.ie.kafka;

public class AVEvent {

	private String timeStamp;
	private String seqkey;
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


	public AVEvent() {
		super();
	}
	
	public AVEvent(String timeStamp, String seqkey, String av_Id, String speed, String batteryLevel, String driverTirenessLevel, String location, String environmentalLightning, String rainConditions,
			String fogConditions, String tractionWheelsLevel, String trafficLight, String obstacleProximity, String pedestrianProximity, String averageConsumptionLevel) {

		this.timeStamp = timeStamp;
		this.seqkey = seqkey;
		this.av_Id = av_Id;
		this.speed = speed;
		this.batteryLevel = batteryLevel;
		this.driverTirenessLevel = driverTirenessLevel;
		this.location = location;
		this.environmentalLightning = environmentalLightning;
		this.rainConditions = rainConditions;
		this.fogConditions = fogConditions;
		this.tractionWheelsLevel = tractionWheelsLevel;
		this.trafficLight = trafficLight;
		this.obstacleProximity = obstacleProximity;
		this.pedestrianProximity = pedestrianProximity;
		this.averageConsumptionLevel = averageConsumptionLevel;
	}


	@Override
	public String toString() {
		return "AvEvent ["+"TimeStamp=" + timeStamp + "; seqkey=" + seqkey + "; av_Id=" + av_Id + "; Speed=" + speed + "; BatteryLevel=" + batteryLevel + "; DriverTirenessLevel=" + driverTirenessLevel + "; Location="
				+ location + "; EnvironmentalLightning=" + environmentalLightning + "; RainConditions=" + rainConditions + "; FogConditions=" + fogConditions + "; TractionWheelsLevel=" + tractionWheelsLevel
				+ "; TrafficLight=" + trafficLight + "; ObstacleProximity=" + obstacleProximity + "; PedestrianProximity=" + pedestrianProximity + "; AverageConsumptionLevel="
				+ averageConsumptionLevel + "]";
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getSeqkey() {
		return seqkey;
	}
	public void setSeqkey(String seqkey) {
		this.seqkey = seqkey;
	}
	public String getAvId() {
		return av_Id;
	}
	public void setAvId(String av_Id) {
		this.av_Id = av_Id;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getBatteryLevel() {
		return batteryLevel;
	}
	public void setBatteryLevel(String batteryLevel) {
		this.batteryLevel = batteryLevel;
	}
	public String getDriverTirenessLevel() {
		return driverTirenessLevel;
	}
	public void setDriverTirenessLevel(String driverTirenessLevel) {
		this.driverTirenessLevel = driverTirenessLevel;
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
	public String getTractionWheelsLevel() {
		return tractionWheelsLevel;
	}
	public void setTractionWheelsLevel(String tractionWheelsLevel) {
		this.tractionWheelsLevel = tractionWheelsLevel;
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