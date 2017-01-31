package com.merchant.models;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONObject;

public class Merchant implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Long id;
	public String name;
	public String UUID;
	public String desc;
	public String businessHours;
	public String phone;
	public String address;
	public Date blockedDate;

	public Merchant(JSONObject json) {
		
		try {
			if(json != null){
				
				if(json.has("merchant")){
					this.blockedDate = new Date(json.optLong("updated"));
					json = json.getJSONObject("merchant");
					
				}
				this.UUID = json.optString("uuid");
				this.id = json.optLong("id");
				this.name = json.optString("name");
				this.phone = json.optString("phone");
				this.address = json.optString("address");
				
				if (!json.isNull("description")) {
					this.desc = json.optString("description","");
				}else{
					this.desc = "";
				}
				if (!json.isNull("businessHours")) {
					this.businessHours = json.optString("businessHours","");
				}else{
					this.businessHours = "";
				}
				
				/*"id": 1, 
	            "name": "Hotel Fortune Landmark", 
	            "email": "fortune-landmark@second-swipe.com", 
	            "status": 1, 
	            "created": 1423649315000, 
	            "updated": 1423649315000, 
	            "lat": "10.20", 
	            "lng": "10.30"*/
			}
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
