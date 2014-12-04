package com.cloudtour.twittmap.web;

public class SnsMessage {
	String lat;
	String lng;
	String type;
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(lat).append(";");
		sb.append(lng).append(";");
		sb.append(type).append(";");
		return sb.toString();
	}
}
