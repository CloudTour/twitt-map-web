package com.cloudtour.twittmap.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SnsCache {
	private static SnsCache instance = new SnsCache();
	private static List<SnsMessage> positiveList = new ArrayList<SnsMessage>();
	private static List<SnsMessage> neutralList = new ArrayList<SnsMessage>();
	private static List<SnsMessage> negativeList = new ArrayList<SnsMessage>();
	
	private SnsCache() {
	}
	
	public static SnsCache getInstance() {
		return instance;
	}
	
	public synchronized void update(SnsMessage snsMsg) {
		if (snsMsg.type.equals("positive")) {
			positiveList.add(snsMsg);
		} else if (snsMsg.type.equals("negative")) {
			negativeList.add(snsMsg);
		} else {
			neutralList.add(snsMsg);
		}
	}
	
	public synchronized SnsResult get() {
		return new SnsResult( new ArrayList<SnsMessage>(positiveList),
				new ArrayList<SnsMessage>(neutralList),
				new ArrayList<SnsMessage>(negativeList));
	}
	
	public synchronized List<SnsMessage> getPositive() {
		return new ArrayList<SnsMessage>(positiveList);
//		List<SnsMessage> list = new ArrayList<SnsMessage>(positiveList);
//		positiveList = new ArrayList<SnsMessage>();
//		return list;
	}
	public synchronized List<SnsMessage> getNegative() {
		return new ArrayList<SnsMessage>(negativeList);
//		List<SnsMessage> list = new ArrayList<SnsMessage>(negativeList);
//		negativeList = new ArrayList<SnsMessage>();
//		return list;
	}
	public synchronized List<SnsMessage> getNeutral() {
		return new ArrayList<SnsMessage>(neutralList);
//		List<SnsMessage> list = new ArrayList<SnsMessage>(neutralList);
//		neutralList = new ArrayList<SnsMessage>();
//		return list;
	}
}
