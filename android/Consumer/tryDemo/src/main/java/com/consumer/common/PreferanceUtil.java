package com.consumer.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.consumer.models.Card;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PreferanceUtil {
	
	private static String KeyCardList = "CardList";
	private static String KeyShouldRunLocationUpdateService = "KeyShouldRunLocationUpdateService";
	private static String KeySelectedCardId = "selectedCardId";
	private static final String KeyRegId = "registration_id";
	private static final String KeyAppVersion = "appVersion";
	
	private String TAG = "PreferanceUtil";
	SharedPreferences mSharedPreferences;
	SharedPreferences.Editor mPrefsEditor;

	Context mContext;
	
	public PreferanceUtil(Context context) {
		mContext = context;
		mSharedPreferences = mContext.getSharedPreferences(Constants.APP_PREFERENCE_FILE, Context.MODE_PRIVATE);
		mPrefsEditor = mSharedPreferences.edit();
	}
	
		public void setCardDetail(Card card) {
			mPrefsEditor.putString("CardNum", card.cardNum);
			mPrefsEditor.putLong(KeySelectedCardId, card.id);
			mPrefsEditor.commit();
		}

		public Long getCardId() {
			
			return mSharedPreferences.getLong(KeySelectedCardId, 0);
		}
		
		public boolean shouldRunLocationUpdateService(){
			return mSharedPreferences.getBoolean(KeyShouldRunLocationUpdateService, true);
		}
		
		public void setCards(JSONArray cards){
			if(cards != null){
				String str = cards.toString();
				mPrefsEditor.putString(KeyCardList, str);
				mPrefsEditor.commit();
			}
		}
		public List<Card> getCards(){
			List<Card> cards = null;
			
			try {
				String str = mSharedPreferences.getString(KeyCardList, "");
				JSONArray jsonArray = new JSONArray(str);
				cards = new ArrayList<Card>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					cards.add(new Card(json));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
			return cards;
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
	        int registeredVersion = mSharedPreferences.getInt(KeyAppVersion, Integer.MIN_VALUE);
	        int currentVersion = SingletonClass.getAppVersion(mContext);
	        if (registeredVersion != currentVersion) {
	            Log.i(TAG, "App version changed.");
	            return "";
	        }
	        return registrationId;
	    }

}
