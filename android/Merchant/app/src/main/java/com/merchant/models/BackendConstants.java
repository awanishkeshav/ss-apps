package com.merchant.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BackendConstants implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	public ArrayList<KeyValueObject> txnTypes, approvalTypes, networkTypes, dateRanges, couponCodeTypes, offerTargetTypes;

	public void setJson(JSONObject json) {

		if (json != null) {
			try {
				
				
				JSONArray targetTypes = json.optJSONArray("OFFER_TARGET_TYPES");
				this.offerTargetTypes = new ArrayList<KeyValueObject>();
				for (int i = 0; i < targetTypes.length(); i++) {
					JSONObject obj = targetTypes.getJSONObject(i);
					String key = obj.names().getString(0);
					String value = obj.getString(key);
					this.offerTargetTypes.add(new KeyValueObject(key, value));
				}
				
				JSONArray codeTypes = json.optJSONArray("COUPON_CODE_TYPES");
				this.couponCodeTypes = new ArrayList<KeyValueObject>();
				for (int i = 0; i < codeTypes.length(); i++) {
					JSONObject obj = codeTypes.getJSONObject(i);
					String key = obj.names().getString(0);
					String value = obj.getString(key);
					this.couponCodeTypes.add(new KeyValueObject(key, value));
				}
				
				JSONArray dateRangesArr = json.optJSONArray("DATE_RANGES");
				this.dateRanges = new ArrayList<KeyValueObject>();
				for (int i = 0; i < dateRangesArr.length(); i++) {
					JSONObject obj = dateRangesArr.getJSONObject(i);
					String key = obj.names().getString(0);
					String value = obj.getJSONObject(key).getString("display");
					this.dateRanges.add(new KeyValueObject(key, value));
				}

				JSONArray approvalTypes = json.optJSONArray("APPROVAL_TYPES");
				this.approvalTypes = new ArrayList<KeyValueObject>();
				for (int i = 0; i < approvalTypes.length(); i++) {
					JSONObject obj = approvalTypes.getJSONObject(i);
					String key = obj.names().getString(0);
					String value = obj.getString(key);
					this.approvalTypes.add(new KeyValueObject(key, value));
				}

				JSONObject netTypes = json.optJSONObject("NETWORK_TYPES");
				this.networkTypes = new ArrayList<KeyValueObject>();
				for (int i = 0; i < netTypes.names().length(); i++) {
					String key = netTypes.names().getString(i);
					String value = netTypes.getString(key);
					this.networkTypes.add(new KeyValueObject(key, value));
				}

				JSONObject txnTypes = json.optJSONObject("TXN_TYPES");
				this.txnTypes = new ArrayList<KeyValueObject>();
				for (int i = 0; i < txnTypes.names().length(); i++) {
					String key = txnTypes.names().getString(i);
					String value = txnTypes.getString(key);
					this.txnTypes.add(new KeyValueObject(key, value));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean isLoaded() {
		if (dateRanges == null || dateRanges.isEmpty()) {
			return false;
		}
		return true;
	}

//	public TxnCategory categoryByName(String catName) {
//		if (categories != null) {
//			for (TxnCategory txnCategory : categories) {
//				if (txnCategory.name.equals(catName)) {
//					return txnCategory;
//				}
//			}
//		}
//		return null;
//	}

	public KeyValueObject actionByKey(String key) {
		if (approvalTypes != null) {
			for (KeyValueObject action : approvalTypes) {
				if (action.key.equals(key)) {
					return action;
				}
			}
		}
		return null;
	}

//	public ArrayList<String> getCategoriesDisplayStrings() {
//		ArrayList<String> list = new ArrayList<String>();
//		if (this.categories != null) {
//			for (TxnCategory cat : this.categories) {
//				list.add(cat.name);
//			}
//		}
//		return list;
//	}
	
	public ArrayList<String> getDateRangeStrings(){
		ArrayList<String> arr = new ArrayList<String>();
		if (this.dateRanges != null) {
			for (KeyValueObject object : this.dateRanges) {
				arr.add(object.value);
			}
		}
		return arr;
	}
	
	public ArrayList<String> getOfferTargetTypeStrings(){
		ArrayList<String> arr = new ArrayList<String>();
		if (this.offerTargetTypes != null) {
			for (KeyValueObject object : this.offerTargetTypes) {
				arr.add(object.value);
			}
		}
		return arr;
	}
	
	public ArrayList<String> getCouponCodeTypeStrings(){
		ArrayList<String> arr = new ArrayList<String>();
		if (this.couponCodeTypes != null) {
			for (KeyValueObject object : this.couponCodeTypes) {
				arr.add(object.value);
			}
		}
		return arr;
	}
	
	public KeyValueObject couponCodeTypeByKey(String key) {
		if (couponCodeTypes != null) {
			for (KeyValueObject obj : couponCodeTypes) {
				if (obj.key.equals(key)) {
					return obj;
				}
			}
		}
		return null;
	}
	
	public KeyValueObject offerTargetTypeByKey(String key) {
		if (offerTargetTypes != null) {
			for (KeyValueObject obj : offerTargetTypes) {
				if (obj.key.equals(key)) {
					return obj;
				}
			}
		}
		return null;
	}

	public ArrayList<String> getActionDisplayStrings() {
		ArrayList<String> list = new ArrayList<String>();
		if (this.approvalTypes != null) {
			for (KeyValueObject obj : this.approvalTypes) {
				list.add(obj.value);
			}
		}

		/*
		 * list.add("ONE"); list.add("Two"); list.add("Three");
		 * list.add("Four");
		 */
		return list;
	}

	public int getActionPosition(String actionKey) {
		int pos = 0;
		if (approvalTypes == null || actionKey == null)
			return pos;
		for (int i = 0; i < approvalTypes.size(); i++) {
			if (actionKey.equalsIgnoreCase(approvalTypes.get(i).key)) {
				pos = i;
				break;
			}
		}

		return pos;
	}

}
