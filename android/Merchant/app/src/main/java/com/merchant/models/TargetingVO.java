package com.merchant.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONObject;

import com.merchant.common.SingletonClass;

public class TargetingVO implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	public Long id, offerId;
	public Long minVisit; 
	public String targetType;
	public Double minTotalSpend;
	

	public TargetingVO(JSONObject json) {
		if (json != null) {
			this.id = json.optLong("id");
			this.minVisit = json.optLong("minVisits",0);
			this.targetType = json.optString("targetType","");
			this.minTotalSpend = json.optDouble("minTotalSpend",0.00);
			this.offerId = new Offer(json.optJSONObject("offer")).id;

		}
	}
	
	public String getTitle(){
		if (targetType != null && !targetType.isEmpty()) {
			KeyValueObject obj = SingletonClass.sharedInstance().getbConstants().offerTargetTypeByKey(targetType);
			if (obj != null) {
				return obj.value;
			}
		}
		return targetType;
	}
	public String getDescription(){
		
		String str = "";
		if ((targetType.isEmpty() || targetType.equals("any")) && minTotalSpend == 0.0 && minVisit == 0) {
			str = "All Privilege Club Members";
		}else{
			String visitStr = "";
			if (minVisit > 0) {
				if (minVisit == 1) {
					visitStr = "1 Visit";
				}else{
					visitStr = SingletonClass.stringFromNumber(minVisit, 0)+" Visits";
				}
			}
			
			String spendStr = "";
			if (minTotalSpend > 0) {
				spendStr = SingletonClass.getPriceString(minTotalSpend);
			}
			
			
			ArrayList<String> strArr = new ArrayList<String>();
			if (!visitStr.isEmpty()) {
				strArr.add(visitStr);
			}
			if (!spendStr.isEmpty()) {
				strArr.add(spendStr);
			}
			
			str = SingletonClass.getStringFromStringArray(strArr, " or ");
		}
		
		return str;
	}

}
