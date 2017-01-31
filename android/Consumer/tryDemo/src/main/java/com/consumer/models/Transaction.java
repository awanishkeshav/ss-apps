package com.consumer.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.consumer.common.SingletonClass;

import android.os.Parcel;
import android.os.Parcelable;

public class Transaction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum ReviewStatus {
		NO_REVIEW, REVIEW_REQUIRED, REVIEWED
	}

	public Date date;
	public Double amount;
	public TxnCategory txnCategory;
	public ReviewStatus reviewStatus;
	public Long id;
	public Long cardId;
	public Merchant merchant;
	public Double reviewCount;
	public ArrayList<Tag> tags;

	public Transaction(Merchant merchant, Date date, Double amount,
			TxnCategory trxType, ReviewStatus reviewStatus) {
		this.merchant = merchant;
		this.date = date;
		this.amount = amount;
		this.txnCategory = trxType;
		this.reviewStatus = reviewStatus;
	}
	
	public Transaction(Long cardId, Long txnId){
		this.reviewStatus = ReviewStatus.NO_REVIEW;
		this.txnCategory = TxnCategory.unknown();
		this.tags = new ArrayList<Tag>();
		this.cardId = cardId;
		this.id = txnId;
	}

	public Transaction(JSONObject json) {

		try {
			if (json != null) {
				this.reviewStatus = ReviewStatus.NO_REVIEW;
				this.txnCategory = TxnCategory.unknown();
				this.tags = new ArrayList<Tag>();

				if (!json.isNull("id")) {
					this.id = json.getLong("id");

				}
				this.cardId = json.optLong("cardId");

				if (!json.isNull("txDate")) {
					this.date = new Date(json.getLong("txDate"));
				}
				if (!json.isNull("amtSpentSS")) {
					this.amount = json.getDouble("amtSpentSS");
				}

				String rStatus = json.optString("reviewStatus");
				if (rStatus != null) {
					if (rStatus.equals("NR")) {
						this.reviewStatus = ReviewStatus.NO_REVIEW;
					} else if (rStatus.equals("RR")) {
						this.reviewStatus = ReviewStatus.REVIEW_REQUIRED;
					} else if (rStatus.equals("RD")) {
						this.reviewStatus = ReviewStatus.REVIEWED;
					}
				}
				JSONObject merchantJson = json.optJSONObject("merchant");
				if (merchantJson != null) {
					this.merchant = new Merchant(merchantJson);
				}
				JSONArray tagJsonArray = json.optJSONArray("tags");
				if (tagJsonArray != null) {
					for (int i = 0; i < tagJsonArray.length(); i++) {
						JSONObject tagJson = tagJsonArray.getJSONObject(i);
						this.tags.add(new Tag(tagJson));
					}
				}
				if (!json.isNull("category")) {
					JSONObject catJson = json.getJSONObject("category");
					this.txnCategory = new TxnCategory(catJson);

				}
				this.reviewCount = json.optDouble("review");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getMerchantName() {
		if (this.merchant != null && this.merchant.name != null) {
			return this.merchant.name;
		}
		return "";
	}

	public String getTagDisplayString() {
		String str = "";
		ArrayList<String> strArr = new ArrayList<String>();
		for (Tag tag : tags) {
			strArr.add(tag.tagName);
		}

		str = SingletonClass.getStringFromStringArray(strArr, ", ");
		return str;
	}
	public String getTagDisplayStringSeperatedByNewLine() {
		String str = "";
		ArrayList<String> strArr = new ArrayList<String>();
		for (Tag tag : tags) {
			String s = tag.tagName; 
			if (s.length() > 0) {
				s= s.substring(0,1).toUpperCase() + s.substring(1);
			}
			strArr.add(s);
		}

		str = SingletonClass.getStringFromStringArray(strArr, "\n");
		return str;
	}

}
