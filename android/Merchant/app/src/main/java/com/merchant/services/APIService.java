package com.merchant.services;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.merchant.common.Constants;
import com.merchant.common.PreferanceUtil;
import com.merchant.common.SingletonClass;
import com.merchant.models.Error;
import com.merchant.widget.WhiteProgressDialog;

public class APIService extends AsyncTask<RestClient, Integer, String> {

	public interface APIServiceListener {
		public abstract void onCompletion(APIService service, Object data,
				Error error);
	}

	Context context;
	public WhiteProgressDialog dialog;
	public boolean mShowDialog = true;

	public int tag;

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	RestClient.RequestMethod methode;
	RestClient client;
	APIServiceListener serviceListener;

	public void cancelRequest() {
		
		this.serviceListener = null;
		this.cancel(true);
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}
	
	public boolean isRequestRunnung(){
		if (this.getStatus() == Status.RUNNING) {
			return true;
		}
		return false;
	}

	public void setServiceListener(APIServiceListener serviceListener) {
		this.serviceListener = serviceListener;
	}

	public APIService(Context context, RestClient client,
			RestClient.RequestMethod methode) {
		this.methode = methode;
		this.context = context;
		if (client != null) {
			this.client = client;
			String merchantUUID = new PreferanceUtil(context).getMerchantUUID();
			if (merchantUUID != null && !merchantUUID.isEmpty() ) {
				client.AddHeader("ssmerchanttoken", merchantUUID);
			}
			
		}
	}

	@Override
	protected void onPreExecute() {
		if (mShowDialog == true) {
			dialog = new WhiteProgressDialog(context, 0);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

	}

	@Override
	protected String doInBackground(RestClient... params) {
		if (params.length > 0) {
			RestClient client = params[0];
			try {

				client.Execute(methode);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		if (dialog != null && dialog.isShowing())
			dialog.dismiss();

		String response = this.client.getResponse();

		Log.d("APIService", "Request: " + this.client.getUrl());
		if (response != null) {
			Log.d("APIService", "Response: " + response);
		} else {
			Log.d("APIService", "Response: null");
		}

		Error error = null;
		String errorMessage = null;
		Object data = null;
		int responseCode = this.client.getResponseCode();

		if (response != null) {

			try {
				JSONObject json = new JSONObject(response);
				boolean success = false;
				if (!json.isNull("success")) {
					success = json.getBoolean("success");
				}
				if (success) {

					if (!json.isNull("data")) {
						data = json.optJSONArray("data");
						if (data == null) {
							data = json.optJSONObject("data");
						}
						if (data == null) {
							data = json.optString("data");
						}

					}

				} else {
					int rCode = responseCode;
					if (!json.isNull("rcode")) {
						rCode = json.getInt("rcode");
					}

					if (!json.isNull("msg")) {
						errorMessage = json.getString("msg");
					}
					error = new Error(rCode, errorMessage);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error = new Error(Error.JsonParsingFailed, "");
			}
		}

		if ((responseCode < 200 || responseCode >= 300)) {
			if (responseCode == 0) {

				if (APIService.isNetworkAvailable(context)) {
					error = new Error(Error.OtherException, "undefined error");

				} else {
					error = new Error(Error.NoInternet,
							"You seem to be offline. Please check your internet connection.");
				}
			} else {
				error = new Error(responseCode, errorMessage);

			}
		}

		this.onCompletion(data, error, null);
	}

	public void getData(boolean isForeground) {
		mShowDialog = isForeground;
		if (SingletonClass.isNetworkAvailable(context))
			this.execute(this.client);
		else
			Toast.makeText(context, "No internet connection found",
					Toast.LENGTH_SHORT).show();
	}

	public void getDataAndWaitForCompletion() {
		;
		this.onPostExecute(this.doInBackground(this.client));
	}

	void onCompletion(Object data, Error error, JSONObject extra) {
		if (this.serviceListener != null) {
			this.serviceListener.onCompletion(this, data, error);
			if (error != null) {
				if (error.getCode() == Error.InvalidSession
						|| error.getCode() == Error.HTTPInvalidSession) {
					// Do logout
					// Intent intent = new Intent(this.context,
					// LoginActivity.class);
					// this.context.startActivity(intent);
				}
			}
			this.serviceListener = null;
		}
	}

	public static boolean isNetworkAvailable(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
		return isConnected;
	}

	public static String getRESTApiBaseUrl() {
		return Constants.getRESTApiBaseUrl();
	}

	public static APIService isMerchantRegistered(Context context) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant";
		RestClient client = new RestClient(url);

		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}
	
	public static APIService getAllMerchant(Context context) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchants";
		RestClient client = new RestClient(url);

		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService registerMerchnat(Context context,
			String merchantUUID, String accessCode) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant/device";
		RestClient client = new RestClient(url);
		client.AddParam("uuid", merchantUUID);
		client.AddParam("accessCode", accessCode);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.POST);
		return service;
	}

	public static APIService getMerchantSummary(Context context,
			String dateRangeKey) {
		String url = APIService.getRESTApiBaseUrl()
				+ "/beapi/merchant/txnSummary";
		RestClient client = new RestClient(url);
		client.AddParam("duration", dateRangeKey);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}
	
	public static APIService getMerchantReviewTemplate(Context context) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant/reviewTemplate";
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService getMerchantReviews(Context context,
			String dateRangeKey) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant/reviews";
		RestClient client = new RestClient(url);
		if (dateRangeKey != null) {
			client.AddParam("duration", dateRangeKey);
		}
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService postReviewResponse(Context context, Long reviewId,
			String response, Long offerId) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant/review/"
				+ reviewId.toString() + "/response";
		RestClient client = new RestClient(url);
		client.AddParam("merchantComment", response);
		if (offerId != null) {
			client.AddParam("offerId", offerId.toString());
		}
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.PUT);
		return service;
	}

	public static APIService getAllOffersFromMerchant(Context context, String status) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant/offers";
		RestClient client = new RestClient(url);
		if (status != null && !status.isEmpty()){
			client.AddParam("status", status);
		}

		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService setReviewTemplate(Context context, String cat1,
			String cat2, String cat3) {
		String url = APIService.getRESTApiBaseUrl()
				+ "/beapi/merchant/reviewTemplate";
		RestClient client = new RestClient(url);
		if (cat1 != null && !cat1.isEmpty()) {
			client.AddParam("criteria1", cat1);
		}
		if (cat2 != null && !cat2.isEmpty()) {
			client.AddParam("criteria2", cat2);
		}
		if (cat3 != null && !cat3.isEmpty()) {
			client.AddParam("criteria3", cat3);
		}
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.PUT);
		return service;
	}

	public static APIService getReviewDetails(Context context, Long reviewId) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant/review/"
				+ reviewId.toString();
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService getMerchantDetails(Context context) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant";
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService updateMerchantProfile(Context context,
			String name, String desc, String hours) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant";
		RestClient client = new RestClient(url);
		if (name != null && !name.isEmpty()) {
			client.AddParam("name", name);
		}
		if (desc == null) {
			client.AddParam("description", "");
		} else {
			client.AddParam("description", desc);
		}
		if (hours == null) {
			client.AddParam("businessHours", "");
		} else {
			client.AddParam("businessHours", hours);
		}
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.PUT);
		return service;
	}

	public static APIService addOrUpdateOfferTarget(Context context,
			Long offerId, Long targetId, String type, Long visit, Double spend) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant/offer/"
				+ offerId.toString() + "/targetting";
		if (targetId != null) {
			url = url + "/" + targetId;
		}
		RestClient client = new RestClient(url);
		JSONObject json = new JSONObject();
		try {
			if (type != null && !type.isEmpty()) {

				json.put("targetType", type);
			}
			if (visit != null) {
				json.put("minVisits", visit);
			}
			if (spend != null) {
				json.put("minTotalSpend", spend);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		client.setJsonObject(json);
		APIService service = null;
		if (targetId != null) {
			service = new APIService(context, client,
					RestClient.RequestMethod.PUT);
		} else {
			service = new APIService(context, client,
					RestClient.RequestMethod.POST);
		}

		return service;
	}

	public static APIService deleteOfferTarget(Context context, Long offerId,
			Long targetId) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant/offer/"
				+ offerId.toString() + "/targetting";
		url = url + "/" + targetId;
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.DELETE);

		return service;
	}

	public static APIService getAllOfferTargets(Context context, Long offerId) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant/offer/"
				+ offerId.toString() + "/targettings";

		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);

		return service;
	}
	
	public static APIService setOfferStatus(Context context, Long offerId, String status) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant/offer/"
				+ offerId.toString() + "/status";
		RestClient client = new RestClient(url);
		client.AddParam("status", status);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.PUT);

		return service;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static APIService getAllTags(Context context, Long cardId) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/consumer/card/"
				+ cardId.toString() + "/txns/taggedSummary";
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService getAllTransactions(Context context, Long cardId,
			String tagIds, String searchParam) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/consumer/card/"
				+ cardId.toString() + "/txns";
		RestClient client = new RestClient(url);
		if (tagIds != null)
			client.AddParam("tagIds", tagIds);
		if (searchParam != null)
			client.AddParam("search", searchParam);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService searchTransactions(Context context, Long cardId,
			String searchParam, int start, int limit) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/consumer/card/"
				+ cardId.toString() + "/txns/search";
		RestClient client = new RestClient(url);
		if (searchParam != null)
			client.AddParam("q", searchParam);
		if (start != -1) {
			client.AddParam("start", start + "");
		}
		if (limit != 0) {
			client.AddParam("limit", limit + "");
		}
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService trackGCMRegistrationId(Context context,
			String regId) {
		String url = APIService.getRESTApiBaseUrl()
				+ "/beapi/consumer/device/gcm/";
		RestClient client = new RestClient(url);
		client.AddParam("registrationId", regId);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.PUT);
		return service;
	}

	public static APIService trackUserResponseApprovedOrDeny(Context context,
			String urlToHit, int approved) {
		String url = urlToHit;
		RestClient client = new RestClient(url);
		client.AddParam("approval", approved + "");
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.PUT);
		return service;
	}

	public static APIService getTxnDetails(Context context, Long cardId,
			Long txnId) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/consumer/card/"
				+ cardId.toString() + "/txn/" + txnId.toString();
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService getTxnMiscDetails(Context context, Long cardId,
			Long txnId) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/consumer/card/"
				+ cardId.toString() + "/txn/" + txnId.toString() + "/miscDtl";
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService getOffers(Context context, Long txnId,
			Long merchantId) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant/"
				+ merchantId.toString() + "/offers";
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService blockUnblockMerchant(Context context,
			Long merchantId, boolean block) {
		String url = APIService.getRESTApiBaseUrl()
				+ "/beapi/consumer/merchant/" + merchantId.toString();
		if (block) {
			url = url + "/lock";
		} else {
			url = url + "/unlock";
		}
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.PUT);
		return service;
	}

	public static APIService getAppConstants(Context context) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/consts";
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService getAllCategories(Context context) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/categories";
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService getCardSettings(Context context, Long cardId) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/consumer/card/"
				+ cardId.toString() + "/prefs";
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService saveCardSettingsLimit(Context context,
			Long cardId, JSONObject limitJson) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/consumer/card/"
				+ cardId.toString() + "/prefLimits";
		Log.v("limitJson", limitJson.toString());
		RestClient client = new RestClient(url);
		client.setJsonObject(limitJson);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.PUT);
		return service;
	}

	public static APIService deleteCardSettingsLimit(Context context,
			Long cardId, JSONObject limitJson) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/consumer/card/"
				+ cardId.toString() + "/delPrefLimits";
		RestClient client = new RestClient(url);
		client.setJsonObject(limitJson);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.PUT);
		return service;
	}

	public static APIService getAllBlockedMerchants(Context context) {
		String url = APIService.getRESTApiBaseUrl()
				+ "/beapi/consumer/blockedMerchants";
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService blockUnblockTransactionType(Context context,
			Long cardId, boolean block, String txnType) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/consumer/card/"
				+ cardId.toString() + "/txType/";
		if (block) {
			url = url + "lock";
		} else {
			url = url + "unlock";
		}
		RestClient client = new RestClient(url);
		client.AddParam("txType", txnType);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.PUT);
		return service;
	}

	public static APIService setTxnTag(Context context, Long txnId, String tag) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/consumer/txn/"
				+ txnId.toString() + "/tag";
		RestClient client = new RestClient(url);
		client.AddParam("tag", tag);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.POST);
		return service;
	}

	public static APIService setTxnTags(Context context, Long txnId,
			String selectedTagIds, String newTag) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/consumer/txn/"
				+ txnId.toString() + "/tags";
		RestClient client = new RestClient(url);
		JSONObject json = new JSONObject();

		try {
			if (selectedTagIds != null) {
				json.put("selected", selectedTagIds);
			}
			if (newTag != null) {
				json.put("new", newTag);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		client.setJsonObject(json);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.POST);
		return service;
	}

	public static APIService getReviewTemplate(Context context, Long merchantId) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant/"
				+ merchantId + "/reviewTemplate";
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService getReviewCountForMerchant(Context context,
			Long merchantId) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant/"
				+ merchantId + "/reviewSummary";
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService addReviewOnTxn(Context context, Long txnId,
			JSONObject json) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/consumer/txn/"
				+ txnId.toString() + "/review";
		RestClient client = new RestClient(url);
		client.setJsonObject(json);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.POST);
		return service;
	}

	public static APIService getReviewOnTxn(Context context, Long txnId) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/consumer/txn/"
				+ txnId.toString() + "/review";
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService getAllOffers(Context context, boolean isNearBy) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant/offers";
		if (isNearBy) {
			url = url + "/nearBy";
		}
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService getOfferDetails(Context context, Long offerId) {
		String url = APIService.getRESTApiBaseUrl() + "/beapi/merchant/offer/"
				+ offerId;
		RestClient client = new RestClient(url);
		APIService service = new APIService(context, client,
				RestClient.RequestMethod.GET);
		return service;
	}

	public static APIService updateUserLocation(Context context, Double lat,
			Double lng) {
		String url = APIService.getRESTApiBaseUrl()
				+ "/beapi/consumer/location";
		RestClient client = new RestClient(url);
		JSONObject json = new JSONObject();
		try {
			json.put("lat", lat);
			json.put("lng", lng);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.setJsonObject(json);

		APIService service = new APIService(context, client,
				RestClient.RequestMethod.PUT);

		return service;
	}

}
