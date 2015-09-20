package com.fairlink.passenger.bean;

public class PlaneInfo {
	private String 		longitude;
	private String 		latitude;
	private String 		altitude;
	private String 		flyspeed;
	private String		groundspeed;
	private String		airtemp;
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public String getFlyspeed() {
		return flyspeed;
	}
	public void setFlyspeed(String flyspeed) {
		this.flyspeed = flyspeed;
	}
	public String getGroundspeed() {
		return groundspeed;
	}
	public void setGroundspeed(String groundspeed) {
		this.groundspeed = groundspeed;
	}
	public String getAirtemp() {
		return airtemp;
	}
	public void setAirtemp(String airtemp) {
		this.airtemp = airtemp;
	}
}
