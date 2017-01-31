package com.consumer.models;

import java.io.Serializable;
import org.json.JSONObject;

public class TxnCategory implements Serializable {

	/**
	 * 
	 */

	public enum Type {
		RETAIL, DINING, FOOD, GAS, GROCERIES, MEDICAL, TRAVEL, UTILITIES, UNKNOWN
	}

	private static final long serialVersionUID = 1L;

	public Long id;
	public String name;
	Long mccCode;
	public Type type;

	public TxnCategory(JSONObject json) {
		if (json != null) {

			this.id = json.optLong("id");
			this.name = json.optString("name");
			this.mccCode = json.optLong("mccCode");
			this.type = Type.UNKNOWN;

			if (this.name != null) {
				String cat = this.name;
				this.type = TxnCategory.categoryTypeByName(cat);
			}
		}
	}
	
	public TxnCategory(String paramName, Type paramType) {
		this.name = paramName;
		this.type = paramType;
	}
	
	TxnCategory() {
	}
	
	private static Type categoryTypeByName(String cat){
		Type type = Type.UNKNOWN;
		
		if (cat.equals("Travel")) {
			type = Type.TRAVEL;
		} else if (cat.equals("Food") || cat.equals("Dining")) {
			type = Type.FOOD;
		} else if (cat.equals("Grossary") || cat.equals("Groceries")) {
			type = Type.GROCERIES;
		} else if (cat.equals("Gas") || cat.equals("Auto & Gas")) {
			type = Type.GAS;
		} else if (cat.equals("Retail")) {
			type = Type.RETAIL;
		} else if (cat.equals("Dining")) {
			type = Type.DINING;
		} else if (cat.equals("Medical")) {
			type = Type.MEDICAL;
		} else if (cat.equals("Utility") || cat.equals("Utilities")) {
			type = Type.UTILITIES;
		}
		return type;
	}

	public static TxnCategory unknown(){
		
		TxnCategory cat = new TxnCategory();
		cat.type = Type.UNKNOWN;
		return cat;
	}

}
