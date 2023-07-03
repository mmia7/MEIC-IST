package org.ie.kafka;

public class Mediator {
	
	String timeStamp;
	String	av_id;
	Integer	speed;  //reaction
	String	takeRest; //reaction
	String	weatherStatus; //road condition
	Integer	tractionWheelsLevel; //reaction
	String location; //map API
	
	public Mediator() {
		super();
		
	}
	
	public Mediator(String timeStamp, String av_id, Integer speed, String takeRest, String weatherStatus, Integer tractionWheelsLevel, String location) {
				
		this.timeStamp = timeStamp;
		this.av_id = av_id;
		this.speed = speed;
		this.takeRest = takeRest;
		this.weatherStatus = weatherStatus;
		this.tractionWheelsLevel = tractionWheelsLevel;
		this.location = location;
    }


	@Override
	public String toString() {
		return "Mediator ["+"TimeStamp=" + timeStamp + "; av_id=" + av_id + "; speed=" + speed 
                + "; takeRest=" + takeRest + "; weatherStatus=" + weatherStatus 
				+ "; tractionWheelsLevel=" + tractionWheelsLevel + "; location=" + location +"]";
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

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	public String getTakeRest() {
		return takeRest;
	}

	public void setTakeRest(String takeRest) {
		this.takeRest = takeRest;
	}
	
	public String getWeatherStatus() {
		return weatherStatus;
	}

	public void setWeatherStatus(String weatherStatus) {
		this.weatherStatus = weatherStatus;
	}

	public Integer getTractionWheelsLevel() {
		return tractionWheelsLevel;
	}

	public void setTractionWheelsLevel(Integer tractionWheelsLevel) {
		this.tractionWheelsLevel = tractionWheelsLevel;
	}	

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}	
	
	
}