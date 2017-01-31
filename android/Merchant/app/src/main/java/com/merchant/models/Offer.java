package com.merchant.models;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Offer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum Status {
		Active, InActive, Expired, Archived
	}
	
	public String title, desc, code, codeType;
	private String imageUrl;
	public Merchant merchant;
	public Date expiryDate, startDate;
	public Long id;
	private Long usedCount, value$;
	public Status status;
	
	public Offer(JSONObject json){
		
		
		try {
			if (json != null) {
				
				this.startDate = new Date();
				status = Status.Active;
				
				String statusStr = json.optString("status","");
				if (statusStr.equals("Suspended")) {
					status = Status.InActive;
					
				}else if (statusStr.equals("Archived")) {
					status = Status.Archived;
				}
				this.code = json.optString("code","");
				this.codeType = json.optString("codeType","");
				this.title = json.optString("title");
				this.desc = json.optString("description");
				if (!json.isNull("imgUrl")) {
					this.imageUrl = json.optString("imgUrl");
				}
				
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

	public Long getUsedCount() {
		if (usedCount == null) {
			usedCount = (long) (Math.random()*10);
		}
		return usedCount;
	}

	public Long getValue$() {
		if (value$ == null) {
			value$ = (long) (Math.random()*100);
		}
		return value$;
	}

}
