package com.consumer.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.consumer.common.SingletonClass;
import com.consumer.models.TxnCategory.Type;

public class Limit implements Serializable{

	/**
	 * 
	 */
	
	public enum Period{
		DAILY, WEEKLY, MONTHLY
	}
	
	private static final long serialVersionUID = 1L;
	private String categoryKey, periodKey;
	public String actionkey;
	public Double userLimit, maxLimit;
	
	public Period period;
	private TxnCategory category;
	private KeyValueObject action;
	
	
	
	public Limit(JSONObject json, String periodKey) {
		if (json != null) {
			
			try {
				actionkey = json.optString("action");
				this.periodKey = periodKey;
				categoryKey = json.optString("categoryKey", null);
				userLimit 	= json.getDouble("userLimit");
				maxLimit 	= json.getDouble("maxLimit");
				
				period = Period.DAILY;
				if (this.periodKey.equalsIgnoreCase("Weekly")) {
					period = Period.WEEKLY;
				}else if (this.periodKey.equalsIgnoreCase("Monthly")) {
					period = Period.MONTHLY;
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Limit() {
		this.period = Period.DAILY;
		BackendConstants bc = SingletonClass.sharedInstance().getbConstants();
		if(bc != null && bc.approvalTypes != null && bc.approvalTypes.size() > 0)
			this.action = SingletonClass.sharedInstance().getbConstants().approvalTypes.get(0);
		this.userLimit = 0.0;
		this.maxLimit = 100.0;
	}

	public static Limit defaultLimit() {
		Limit limit = new Limit();
		return limit;
	}
	
	public void reset(){
		this.userLimit = 0.0;
		this.action = SingletonClass.sharedInstance().getbConstants().approvalTypes.get(0);
		this.actionkey = this.action.key;
	}

	public TxnCategory getCategory() {
		if(category == null){
			category = SingletonClass.sharedInstance().getbConstants().categoryByName(categoryKey);
		}
		return category;
	}
	

	public String getCategoryKey() {
		return categoryKey;
	}

	public void setCategoryKey(String categoryKey) {
		this.categoryKey = categoryKey;
	}

	public void setAction(KeyValueObject action) {
		this.action = action;
	}
	

	public KeyValueObject getAction() {
		if(action == null){
			action = SingletonClass.sharedInstance().getbConstants().actionByKey(actionkey);
		}
		return action;
	}
	
	public int getLimitPercentValue(){
		return (int) ((this.userLimit*100)/this.maxLimit);
	}
	public void setLimitPercentValue(int valueOutOf100){
		this.userLimit = (valueOutOf100*this.maxLimit)/100.0;
	}
	
	public JSONObject getJsonObject(){
		JSONObject json = new JSONObject();
		try {
			if(this.userLimit >= 0)
				json.put("userLimit", this.userLimit);
			
			if(this.getAction() != null){
				json.put("action", this.getAction().key);
			}
			if(this.getCategory() != null && this.category.type != Type.UNKNOWN){
				json.put("categoryKey", this.category.name);
			}else if(this.categoryKey != null && !this.categoryKey.isEmpty()){
				json.put("categoryKey", this.categoryKey);
			}else{
				json.put("categoryKey", "Any");
			}
			String pKey = Limit.getPeroidString(this.period);
			json.put("periodKey", pKey);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
	
	public static String getPeroidString(Period period){
		String pKey = "";
		switch (period) {
		case DAILY:	
			pKey = "Daily";
			break;
		case WEEKLY:	
			pKey = "Weekly";
			break;
		case MONTHLY:	
			pKey = "Monthly";
			break;
		}
		return pKey;
	}

}
