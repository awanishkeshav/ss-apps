package com.consumer.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.consumer.R;
import com.consumer.adapters.CategoryAdapter;
import com.consumer.models.TxnCategory.Type;

public class BackendConstants implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	public ArrayList<TxnCategory> categories;
	public ArrayList<KeyValueObject> txnTypes, approvalTypes, networkTypes;

	public void setJson(JSONObject json) {

		if (json != null) {
			try {

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
		if (categories == null || categories.isEmpty() || approvalTypes == null
				|| approvalTypes.isEmpty()) {
			return false;
		}
		return true;
	}

	public TxnCategory categoryByName(String catName) {
		if (categories != null) {
			for (TxnCategory txnCategory : categories) {
				if (txnCategory.name.equals(catName)) {
					return txnCategory;
				}
			}
		}
		return null;
	}

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

	public ArrayList<String> getCategoriesDisplayStrings() {
		ArrayList<String> list = new ArrayList<String>();
		if (this.categories != null) {
			for (TxnCategory cat : this.categories) {
				list.add(cat.name);
			}
		}
		return list;
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

	public static int getIconForCategory(TxnCategory cat) {
		int icon = 0;
		if (cat != null) {
			switch (cat.type) {
			case TRAVEL:
				icon = (R.drawable.icon_category_travel);
				break;
			case RETAIL:
				icon = (R.drawable.icon_category_clothing);
				break;
			case DINING:
				icon = (R.drawable.icon_category_dining);
				break;
			case FOOD:
				icon = (R.drawable.icon_category_food);
				break;
			case GAS:
				icon = (R.drawable.icon_category_gas);
				break;
			case GROCERIES:
				icon = (R.drawable.icon_category_groceries);
				break;
			case MEDICAL:
				icon = (R.drawable.icon_category_pharmacy);
				break;
			case UTILITIES:
				icon = (R.drawable.icon_category_utilities);
				break;
			case UNKNOWN:
				icon = (0);
				break;
			}
		}
		return icon;
	}
	
	public static ArrayList<AnalysisVO> getDummyAnalysisList(){
		ArrayList<AnalysisVO> list = new ArrayList<AnalysisVO>();
		
		TxnCategory cat = new TxnCategory("Dining & Entertainment", Type.DINING);
		AnalysisVO vo = new AnalysisVO(cat, 718.93, 38, 36);
		list.add(vo);
		
		cat = new TxnCategory("Groceries", Type.GROCERIES);
		vo = new AnalysisVO(cat, 435.21, 23, 27);
		list.add(vo);
		
		cat = new TxnCategory("Travel", Type.TRAVEL);
		vo = new AnalysisVO(cat, 389.62, 20, 17);
		list.add(vo);
		
		cat = new TxnCategory("Retail Shopping", Type.RETAIL);
		vo = new AnalysisVO(cat, 217.15, 11, 18);
		list.add(vo);
		
		cat = new TxnCategory("Auto & Gas", Type.GAS);
		vo = new AnalysisVO(cat, 148.77, 8, 4);
		list.add(vo);
		
		return list;
	}

}
