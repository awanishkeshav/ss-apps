package com.merchant.models;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.merchant.utility.DateUtil;

public class Review implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Date date;
	public Long id;
	
	public Double avgRating = 0.0, criteria1, criteria2, criteria3;
	public String comment;
	
	private String title;
	
	
	public ReviewTemplate reviewTemplate;

	

	public Review(JSONObject json) {

		try {
			if (json != null) {
				if (!json.isNull("id")) {
					this.id = json.getLong("id");
				}
				
				if (!json.isNull("updated")) {
					this.date = new Date(json.getLong("updated"));
				}
				
				this.comment = json.optString("comment");
				this.reviewTemplate = new ReviewTemplate(json);
				this.criteria1 = json.optDouble("criteria1Value",0.0);
				this.criteria2 = json.optDouble("criteria2Value",0.0);
				this.criteria3 = json.optDouble("criteria3Value",0.0);
				this.avgRating = (this.criteria1 + this.criteria2 + this.criteria3)/3.0;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getReviewTitle() {
		
		if (title == null) {
			if(DateUtil.isToday(date)){
				title = "Today";
			}else if (DateUtil.isYesterday(date)) {
				title = "Yesterday";
			}else if (DateUtil.isTwoDaysAgo(date)) {
				title = "Two Days Ago";
			}else if (DateUtil.isLastWeek(date)) {
				title = "Last Week";
			}else if (DateUtil.isLastMonth(date)) {
				title = "Last Month";
			}else{
				title = "Older";
			}
		}
		
		return title;
	}

}
