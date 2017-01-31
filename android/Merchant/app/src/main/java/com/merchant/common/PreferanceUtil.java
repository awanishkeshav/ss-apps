package com.merchant.common;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PreferanceUtil {

	private static String KeyMerchantName = "KeyMerchantJSON";
	private static String KeyMerchantUUID = "KeyMerchantUUId";

	private static final String KeyRegId = "registration_id";
	private static final String KeyAppVersion = "appVersion";

	private String TAG = "PreferanceUtil";
	SharedPreferences mSharedPreferences;
	SharedPreferences.Editor mPrefsEditor;

	Context mContext;

	public PreferanceUtil(Context context) {
		mContext = context;
		mSharedPreferences = mContext.getSharedPreferences(
				Constants.APP_PREFERENCE_FILE, Context.MODE_PRIVATE);
		mPrefsEditor = mSharedPreferences.edit();
	}
	
	public void setMerchantUUID(String uuid) {

		mPrefsEditor.putString(KeyMerchantUUID, uuid);
		mPrefsEditor.commit();
	}

	public String getMerchantUUID() {

		return mSharedPreferences.getString(KeyMerchantUUID, "");
	}

	public void setMerchantName(String name) {

		mPrefsEditor.putString(KeyMerchantName, name);
		mPrefsEditor.commit();
	}

	public String getMerchantName() {

		return mSharedPreferences.getString(KeyMerchantName, "");
	}

	public void storeRegistrationId(String regId) {

		int appVersion = SingletonClass.getAppVersion(mContext);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		mPrefsEditor.putString(KeyRegId, regId);
		mPrefsEditor.putInt(KeyAppVersion, appVersion);
		mPrefsEditor.commit();
	}

	public String getRegistrationId() {

		String registrationId = mSharedPreferences.getString(KeyRegId, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = mSharedPreferences.getInt(KeyAppVersion,
				Integer.MIN_VALUE);
		int currentVersion = SingletonClass.getAppVersion(mContext);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	public Long getCardId() {
		// TODO Auto-generated method stub
		return null;
	}

}
