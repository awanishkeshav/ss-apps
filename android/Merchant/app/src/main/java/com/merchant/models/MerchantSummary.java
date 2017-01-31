package com.merchant.models;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONObject;

public class MerchantSummary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Long visitCount, reviewCount;
	
	public Double totalSpend, avgRating;
	

	public MerchantSummary(JSONObject json) {
		
		try {
			if(json != null){
				visitCount = json.optLong("visits",0);
				reviewCount = json.optLong("reviews",0);
				totalSpend = json.optDouble("spend",0.0);
				avgRating = json.optDouble("reviewsAverage",0);
			}
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
