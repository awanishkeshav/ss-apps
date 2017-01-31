package com.consumer.models;

import java.io.Serializable;
import java.text.NumberFormat;

import org.json.JSONObject;

public class Tag implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Long id;
	public String tagName;
	public Double total;
	public boolean isSelected;
	
	public Tag(JSONObject json) {
		
		try {
			if(json != null){
				this.isSelected = false;
				this.id = json.optLong("id");
				this.tagName = json.optString("tagName");
				if(this.tagName == null || this.tagName.isEmpty())
					this.tagName = json.optString("tag");
				Object total = json.opt("total");
				if(total != null){
					this.total = Double.parseDouble(total+"");
				}
			}
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getTagDisplayString(){
		return this.tagName + " (" + NumberFormat.getCurrencyInstance().format(this.total) + ")";
	}

}
