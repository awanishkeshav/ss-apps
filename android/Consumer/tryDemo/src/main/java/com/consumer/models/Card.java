package com.consumer.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Serializable{
	
	/**
	 * 
	 */
	
	
	private static final long serialVersionUID = 1L;

	public enum CardNetwork {
		MASTER, VISA, UNKNOWN
	}
	public enum Status {
		ACTIVE, INACTIVE
	}
	public enum CardType {
	     CREDIT, DEBIT, UNKNOWN
	}
	public String cardTitle;
	public Double limit, avaialbleLimit, currOS, amtSpentSS;
	public String cardNum;
	public CardNetwork cardNetwork;
	public Status status;
	public CardType cardType;
	public Long id;
	
	
	
	public Card(String cardTitle, Double limit, Double avaialbleLimit, Double currOS, Double amtSpentSS, String cardNum,
			CardNetwork cardNetwork, Status status, CardType cardType, Long id) {
		super();
		this.cardTitle = cardTitle;
		this.limit = limit;
		this.avaialbleLimit = avaialbleLimit;
		this.currOS = currOS;
		this.amtSpentSS = amtSpentSS;
		this.cardNum = cardNum;
		this.cardNetwork = cardNetwork;
		this.status = status;
		this.cardType = cardType;
		this.id = id;
	}

	public Card(JSONObject json) {
		
		try {
			if(json != null){
				
				
				this.id 		= json.optLong("id");
				this.cardNum	= json.optString("cardNum", "");
				this.cardTitle	= json.optString("cardTitle", "");
				
				Object limit = json.opt("limit");
				if(limit != null){
					this.limit		= Double.parseDouble(limit +"");
				}
				
				Object avaialbleLimit = json.opt("avaialbleLimit");
				if(avaialbleLimit != null){
					this.avaialbleLimit		= Double.parseDouble(avaialbleLimit +"");
				}
				
				Object amtSpentSS = json.opt("amtSpentSS");
				if(amtSpentSS != null){
					this.amtSpentSS		= Double.parseDouble(amtSpentSS +"");
				}
				
				Object currOS = json.opt("currOS");
				if(limit != null){
					this.currOS		= Double.parseDouble(currOS +"");
				}
				
				
			
				this.cardNetwork = CardNetwork.UNKNOWN;
		    	if(!json.isNull("cardNetwork")){
		    		String network = json.getString("cardNetwork");
		    		
		    		if(network.equals("Master")){
		    			this.cardNetwork = CardNetwork.MASTER;
		    		} else if(network.equals("Visa")){
		    			this.cardNetwork = CardNetwork.VISA;
		    		} 
		    	}
		    	this.cardType = CardType.UNKNOWN;
		    	if(!json.isNull("cardType")){
		    		String type = json.getString("cardType");
		    		
		    		if(type.equals("Credit card")){
		    			this.cardType = CardType.CREDIT;
		    		}else if(type.equals("Debit card")){
		    			this.cardType = CardType.DEBIT;
		    		}  
		    	}
		    	this.status		= json.optInt("status") == 1? Status.ACTIVE : Status.INACTIVE;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    
	}
	
	public String getCardDisplayString(){
		if(cardNum!=null && cardNum.length()>14)
			return this.cardTitle + " (..."+this.cardNum.substring(this.cardNum.length()-4,this.cardNum.length())+")";
		else
			return "";
	}
	
}
