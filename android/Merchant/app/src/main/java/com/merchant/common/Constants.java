package com.merchant.common;

/*
1. Remove Log calls
2. Remove android:debuggable attribute from your manifest file. 
3. verify <uses-permission> element im manifest
4. Provide values for the android:versionCode and android:versionName attributes
5. Switch to prod server
6. Review Constants.java
7. Review the contents of assets/ directory and res/raw/ directory for raw asset files and static files that need to update or remove
8. Review DB schema version

Get a private key for signing application*/

public class Constants {

	public enum Bank {
		SBI, IndusInd
	}

	public static Bank bank = Bank.SBI;
	
	public static boolean MODE_DEBUG = false;
	public static boolean MODE_PRODUCTION = true;

	public static final String APP_PREFERENCE_FILE = "secondswipe_pref_";
	public static final String PREF_USERNAME = "pref_username";
	public static final String PREF_SESSION_ID = "pref_session_id";
	public static final String PREF_USER_OBJECT = "pref_user_object";
	public static final String PREF_ALL_CONSTANTS = "pref_all_constants";
	public static final String DotString = "•";
	
	
	public static final String MERCHANT_UUID = "bigbazaar";

	public static String getRESTApiBaseUrl() {
		if(MODE_PRODUCTION)
			return "http://ec2-54-174-162-49.compute-1.amazonaws.com";
		else
			return "http://ec2-54-174-162-49.compute-1.amazonaws.com";
	}
}
