package com.consumer.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.consumer.models.Transaction.ReviewStatus;

public class Offer implements Serializable{

	/**
	 * 
	 */
	
	public enum Status {
		NEW,READ,UNKNOWN
	}
	
	private static final long serialVersionUID = 1L;
	
	public String title, desc, codeType, code;
	private String imageUrl;
	public TxnCategory category;
	public Merchant merchant;
	public Date expiryDate;
	public Long id;
	public Double distance;
	public Status status;
	
	public Offer(JSONObject json){
		
		
		try {
			if (json != null) {
				status = Status.UNKNOWN;
				if (json.has("offer")) {
					String statusStr = json.optString("status");
					if (statusStr.equals("New")) {
						status = Status.NEW;
					}else if(statusStr.equals("Read")){
						status = Status.READ;
					}
					json = json.optJSONObject("offer");
				}
				
				this.title = json.optString("title");
				this.desc = json.optString("description");
				this.codeType = json.optString("codeType");
				this.code = json.optString("code");
				this.category = TxnCategory.unknown();
				if (!json.isNull("imgUrl")) {
					this.imageUrl = json.optString("imgUrl");
				}
				
				this.distance = json.optDouble("distance",0.0);
				if (!json.isNull("id")) {
					this.id = json.getLong("id");

				}

				if (!json.isNull("endDate")) {
					this.expiryDate = new Date(json.getLong("endDate"));
				}
				
				JSONObject merchantJson = json.optJSONObject("merchant");
				if (merchantJson != null) {
					this.merchant = new Merchant(merchantJson);
				}
				
				if (!json.isNull("category")) {
					JSONObject catJson = json.getJSONObject("category");
					this.category = new TxnCategory(catJson);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public String getImageUrl() {
		if (imageUrl != null && !imageUrl.isEmpty()) {
			if (!imageUrl.startsWith("http")) {
				imageUrl = "http://demo.secondswipe.com/media/"+imageUrl;
			}
		}
		return imageUrl;
	}

}
