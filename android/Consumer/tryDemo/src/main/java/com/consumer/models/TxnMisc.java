package com.consumer.models;

import java.io.Serializable;

import org.json.JSONObject;

public class TxnMisc implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Long visitsOnMerchant;
	public Double monthlySpendOnCategory, totalSpendOnMerchant;
	public boolean isMerchantBlocked;
	
	public TxnMisc(JSONObject json){
		this.visitsOnMerchant = json.optLong("visitsOnMerchant");
		this.monthlySpendOnCategory = json.optDouble("monthlyOnCategory");
		this.totalSpendOnMerchant = json.optDouble("totalOnMerchant");
	}

}
