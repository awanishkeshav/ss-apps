package com.consumer.models;

import java.io.Serializable;

import org.json.JSONObject;

public class ReviewTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String criteria1, criteria2, criteria3;
	public boolean isCommentRequired = false;
	public Merchant merchant;

	public ReviewTemplate(JSONObject json) {

		try {
			if (json != null) {
				this.criteria1 = json.optString("criteria1");
				this.criteria2 = json.optString("criteria2");
				this.criteria3 = json.optString("criteria3");
				int isCmtReq = json.optInt("commentRequired");
				if (isCmtReq == 1) {
					this.isCommentRequired = true;
				}
				JSONObject merchantJson = json.optJSONObject("merchant");
				if (merchantJson != null) {
					this.merchant = new Merchant(merchantJson);
				}
				
				/*this.criteria1 = "Food Quality";
				this.criteria2 = "Service";
				this.criteria3 = "Cleanliness";
				this.isCommentRequired = true;
				*/
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
