package com.consumer.utility;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Generic JSON generator class
 * 
 * @author gourav.jain
 * 
 */
public class JSONUtil {
	/**
	 * Generates JSON object from name value pair.
	 * 
	 * @param nameValuePair
	 * @return
	 */
	public static JSONObject getJSONObject(String... nameValuePair) {
		JSONObject jsonObject = null;

		if (null != nameValuePair && nameValuePair.length % 2 == 0) {

			jsonObject = new JSONObject();
			try {
				int i = 0;
				while (i < nameValuePair.length) {
					if (null != nameValuePair[i]) {
						jsonObject.put(nameValuePair[i], nameValuePair[i + 1]);
						i += 2;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return jsonObject;
	}

	public static JSONObject getNamedJSONObject(String name, JSONObject jsonObject) {
		JSONObject requestJSObj = new JSONObject();
		try {
			requestJSObj.put(name, jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return requestJSObj;
	}

	public static JSONObject getNamedJSONObject(String name, JSONArray jsonArray) {
		JSONObject requestJSObj = new JSONObject();
		try {
			requestJSObj.put(name, jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return requestJSObj;
	}

	/**
	 * Generates JSON array from string array
	 * 
	 * @param names
	 * @return
	 */
	public static JSONArray getJSONArray(String... names) {
		JSONArray jsonArray = new JSONArray();

		if (null != names) {
			for (int i = 0; i < names.length; i++) {
				jsonArray.put(names[i]);
			}
		}

		return jsonArray;
	}

	/**
	 * Generates JSON array from string array
	 * 
	 * @param names
	 * @return
	 */
	public static JSONArray getJSONArray(List<String> names) {
		JSONArray jsonArray = new JSONArray();

		if (null != names) {
			for (int i = 0; i < names.size(); i++) {
				jsonArray.put(names.get(i));
			}
		}

		return jsonArray;
	}

	public static JSONArray getNamedJSONArray(String name, JSONObject jsonObject) {
		JSONArray requestArray = new JSONArray();

		if (null != name && jsonObject != null) {
			requestArray.put(jsonObject);
		}

		return requestArray;
	}
}
