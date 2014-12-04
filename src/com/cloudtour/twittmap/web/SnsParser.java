package com.cloudtour.twittmap.web;

public class SnsParser {
	public SnsMessage parse(String msg) {
		// find the message start
		int left = msg.indexOf("\"Message\""); 
		if (left == -1)
			return null;
		
		left = msg.indexOf('{', left);
		int right = msg.indexOf('}', left);
		return parseMessage(msg.substring(left + 1, right));
	}
	
	private SnsMessage parseMessage(String message) {
		SnsMessage snsMsg = new SnsMessage();
		String[] items = message.split(",");
		for (int i = 0; i < items.length; ++i) {
			String[] pairs = items[i].split(":");
			
			if (pairs[0].equals("\\\"sLatitude\\\"")) {
				snsMsg.lat = pairs[1];
			} else if (pairs[0].equals("\\\"sLongitude\\\"")) {
				snsMsg.lng = pairs[1];
			} else if (pairs[0].equals("\\\"type\\\"")) {
				snsMsg.type = pairs[1].substring(2, pairs[1].length() - 2);
			}
		}
		
		return snsMsg;
	}
}
