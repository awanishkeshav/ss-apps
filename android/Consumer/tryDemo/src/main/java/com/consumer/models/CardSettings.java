package com.consumer.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.consumer.models.Limit.Period;

public class CardSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String TXN_TYPE_ONLIN = "Online";
	public static final String TXN_TYPE_INTERNATIONAL = "Intenational";
			
	// Static list for Daily and Montly generic limit
	public ArrayList<Limit>commonLimits = new ArrayList<Limit>();
	
	// Dynamic list for any peroid with any category
	public ArrayList<Limit>customLimist = new ArrayList<Limit>();
	
	public boolean isOnlineAllowed, isInternationalAllowed; 
	
	
	public CardSettings(JSONObject json){
		if(json != null){
			try {
				
				JSONArray customLimitsArray = json.optJSONArray("customLimits");
				this.customLimist = new ArrayList<Limit>();
				if(customLimitsArray !=null){
					for (int j = 0; j < customLimitsArray.length(); j++) {
						JSONObject customLimitsObj = customLimitsArray.optJSONObject(j);
						for (int i = 0; i < customLimitsObj.names().length(); i++) {
							String key = customLimitsObj.names().getString(i);
							JSONObject value = customLimitsObj.getJSONObject(key);
							this.customLimist.add(new Limit(value, key));
						}
					}
					
				}
				
				
				JSONObject limitsObj = json.optJSONObject("limits");
				this.commonLimits = new ArrayList<Limit>();
				if(limitsObj != null){
					for (int i = 0; i < limitsObj.names().length(); i++) {
						String key = limitsObj.names().getString(i);
						JSONObject value = limitsObj.getJSONObject(key);
						this.commonLimits.add(new Limit(value, key));
					}
				}
				
				JSONObject txnTypes = json.optJSONObject("txTypes");
				
				int international = Integer.parseInt(txnTypes.get("International")+"");
				if(international == 1){
					isInternationalAllowed = true;
				}
				
				int online = Integer.parseInt(txnTypes.get("Online")+"");
				if(online == 1){
					isOnlineAllowed = true;
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	
	public Limit getDailyLimit(){
		  Limit dailyLmit = null;
		  if(commonLimits != null){
		   for (Limit limit : commonLimits) {
		    if (limit.period == Period.DAILY) {
		     dailyLmit = limit;
		    }
		   }
		  }
		  
		  return dailyLmit;
		 }
		 
	public Limit getMonthlyLimit(){
		  Limit monthlyLmit = null;
		  if(commonLimits != null){
		   for (Limit limit : commonLimits) {
		    if (limit.period == Period.MONTHLY) {
		     monthlyLmit = limit;
		    }
		   }
		  }
		  return monthlyLmit;
		 }
	
}
