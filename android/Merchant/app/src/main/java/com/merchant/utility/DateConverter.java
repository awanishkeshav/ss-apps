package com.merchant.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;

@SuppressLint("SimpleDateFormat")
public class DateConverter {
	private static final String yyyy_MM_dd_hh_mm_ss = "yyyy-MM-dd hh:mm:ss";
	public static final String MMMM_dd_yyyy = "MMMM dd, yyyy";
	public static final String yyyy_MM_dd = "yyyy-MM-dd";

	private Calendar calendar;
	private SimpleDateFormat yyyy_MM_dd_hh_mm_ss_SDF = new SimpleDateFormat(yyyy_MM_dd_hh_mm_ss);
	private SimpleDateFormat MMMM_dd_yyyy_SDF = new SimpleDateFormat(MMMM_dd_yyyy);
	private SimpleDateFormat yyyy_MM_dd_SDF = new SimpleDateFormat(yyyy_MM_dd);

	public enum DateFormat {
		yyyy_MM_dd_hh_mm_ss, MMMM_dd_yyyy, yyyy_MM_dd;
	}

	private static DateConverter dateConverter;

	private DateConverter() {
		calendar = Calendar.getInstance();
	}

	public static DateConverter getInstance() {
		if (null == dateConverter) {
			dateConverter = new DateConverter();
		}
		return dateConverter;
	}

	public static void deleteInstance() {
		dateConverter = null;
	}

	public DateConverter setTimestamp(long unixTimestamp) {
		calendar.setTimeInMillis(unixTimestamp * 1000);
		return dateConverter;
	}

	public long getUnixTimestamp() {
		return calendar.getTimeInMillis() / 1000l;
	}

	public DateConverter parse(DateFormat dateFormat, String dateString) {
		if (null != dateFormat && null != dateString) {
			try {
				Date date;
				switch (dateFormat) {
				case yyyy_MM_dd_hh_mm_ss:
					date = yyyy_MM_dd_hh_mm_ss_SDF.parse(dateString);
					break;
				case MMMM_dd_yyyy:
					date = MMMM_dd_yyyy_SDF.parse(dateString);
					break;
				case yyyy_MM_dd:
					date = yyyy_MM_dd_SDF.parse(dateString);
					break;

				default:
					date = new Date();
					break;
				}
				calendar.setTime(date);
			} catch (ParseException e) {
				e.printStackTrace();
				calendar.setTime(new Date());
			}
		}
		return dateConverter;
	}

	public String getDate(DateFormat dateFormat) {
		if (null != dateFormat) {
			switch (dateFormat) {
			case yyyy_MM_dd_hh_mm_ss:
				return yyyy_MM_dd_hh_mm_ss_SDF.format(calendar.getTime());
			case MMMM_dd_yyyy:
				return MMMM_dd_yyyy_SDF.format(calendar.getTime());
			case yyyy_MM_dd:
				return yyyy_MM_dd_SDF.format(calendar.getTime());

			default:
				break;
			}
		}
		return yyyy_MM_dd_hh_mm_ss_SDF.format(calendar.getTime());
	}
	
	public static String getStringFromDate(Context context, Date date){
		
		Date now = new Date();
		long diff = now.getTime() - date.getTime();
		if(diff <= 0){
			return "Just now";
		}
		String str = (String) DateUtils.getRelativeDateTimeString(

		        context, // Suppose you are in an activity or other Context subclass

		        date.getTime(), // The time to display

		        DateUtils.MINUTE_IN_MILLIS, // The resolution. This will display only 
		                                        // minutes (no "3 seconds ago") 


		        DateUtils.WEEK_IN_MILLIS, // The maximum resolution at which the time will switch 
		                         // to default date instead of spans. This will not 
		                         // display "3 weeks ago" but a full date instead

		        0); // Eventual flags
		return str;
	}
	
	public static String getStringFromDateWitoutTime(Context context, Date date){
		
		return (String) android.text.format.DateFormat.format("dd/MM/yyyy", date);
		
	}
	
	public static String getStringFromDateMMDDYY(Date date){
		
		return (String) android.text.format.DateFormat.format("MMMM d, yyyy", date);
		
	}
}
