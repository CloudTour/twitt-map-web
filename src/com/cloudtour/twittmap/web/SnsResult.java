package com.cloudtour.twittmap.web;

import java.sql.Timestamp;
import java.util.List;

public class SnsResult {
		private List<SnsMessage> positiveList;
		private List<SnsMessage> neutralList;
		private List<SnsMessage> negativeList;
		public SnsResult(List<SnsMessage> positiveList,
				List<SnsMessage> neutralList, 
				List<SnsMessage> negativeList) {
			this.positiveList = positiveList;
			this.negativeList = negativeList;
			this.neutralList = neutralList;
		}

		public List<SnsMessage> getPositiveList() {
			return positiveList;
		}

		public List<SnsMessage> getNegativeList() {
			return negativeList;
		}

		public List<SnsMessage> getNeutralList() {
			return neutralList;
		}
}
