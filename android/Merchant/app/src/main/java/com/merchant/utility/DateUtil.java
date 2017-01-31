package com.merchant.utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.merchant.models.Review;;

public class DateUtil {

	public static final String  KEY_TODAY = "Today";
	public static final String  KEY_YESTERDAY = "Yesterday";
	public static final String  KEY_TWO_DAYS_AGO = "Two days ago";
	public static final String  KEY_LAST_WEEK = "Last week";
	public static final String  KEY_LAST_MONTH = "Last month";
	public static final String  KEY_OLDER = "Older";
	
	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
	 public static boolean isSameDay(Date date1, Date date2) {
	        if (date1 == null || date2 == null) {
	            throw new IllegalArgumentException("The dates must not be null");
	        }
	        Calendar cal1 = Calendar.getInstance();
	        cal1.setTime(date1);
	        Calendar cal2 = Calendar.getInstance();
	        cal2.setTime(date2);
	        return isSameDay(cal1, cal2);
	    }
	 public static boolean isToday(Date date) {
	        return isSameDay(date, Calendar.getInstance().getTime());
	    }
	 public static boolean isBeforeDay(Date date1, Date date2) {
	        if (date1 == null || date2 == null) {
	            throw new IllegalArgumentException("The dates must not be null");
	        }
	        Calendar cal1 = Calendar.getInstance();
	        cal1.setTime(date1);
	        Calendar cal2 = Calendar.getInstance();
	        cal2.setTime(date2);
	        return isBeforeDay(cal1, cal2);
	    }
	    
	 public static boolean isBeforeDay(Calendar cal1, Calendar cal2) {
	        if (cal1 == null || cal2 == null) {
	            throw new IllegalArgumentException("The dates must not be null");
	        }
	        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true;
	        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false;
	        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true;
	        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return false;
	        return cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR);
	    }
	 public static boolean isAfterDay(Date date1, Date date2) {
	        if (date1 == null || date2 == null) {
	            throw new IllegalArgumentException("The dates must not be null");
	        }
	        Calendar cal1 = Calendar.getInstance();
	        cal1.setTime(date1);
	        Calendar cal2 = Calendar.getInstance();
	        cal2.setTime(date2);
	        return isAfterDay(cal1, cal2);
	    }
	 public static boolean isAfterDay(Calendar cal1, Calendar cal2) {
	        if (cal1 == null || cal2 == null) {
	            throw new IllegalArgumentException("The dates must not be null");
	        }
	        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false;
	        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true;
	        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false;
	        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return true;
	        return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
	    }
	 
	 public static Calendar firstDayOfLastWeek(Calendar c)
	    {
	        c = (Calendar) c.clone();
	        c.setFirstDayOfWeek(Calendar.MONDAY);
	        // last week
	        c.add(Calendar.WEEK_OF_YEAR, -1);
	        // first day
	        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
	        return c;
	    }

	    public static Calendar lastDayOfLastWeek(Calendar c)
	    {
	        c = (Calendar) c.clone();
	        c.setFirstDayOfWeek(Calendar.MONDAY);
	        // first day of this week
	        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
	        // last day of previous week
	        c.add(Calendar.DAY_OF_MONTH, -1);
	        return c;
	    }
	    public static Calendar lastDayOfCurrentMonth(Calendar c)
	    {
	    	Calendar calendar = (Calendar) c.clone();
	        
	        calendar.add(Calendar.MONTH, 1);  
	        calendar.set(Calendar.DAY_OF_MONTH, 1);  
	        calendar.add(Calendar.DATE, -1);  
	        return calendar;
	    }
	    public static Calendar firstDayOfCurrentMonth(Calendar c)
	    {
	    	Calendar calendar = (Calendar) c.clone();
	        calendar.set(Calendar.DAY_OF_MONTH, 1);  
	        
	        return calendar;
	    }
	    public static Calendar lastDayOfLastMonth(Calendar c)
	    {
	    	Calendar calendar = (Calendar) c.clone();
	        
	        calendar.add(Calendar.MONTH, -2);  
	        calendar.set(Calendar.DAY_OF_MONTH, 1);  
	        calendar.add(Calendar.DATE, 1);  
	        return calendar;
	    }
	    public static Calendar firstDayOfLastMonthMonth(Calendar c)
	    {
	    	Calendar calendar = (Calendar) c.clone();
	        
	        calendar.add(Calendar.MONTH, -1);  
	        calendar.set(Calendar.DAY_OF_MONTH, 1);   
	        return calendar;
	    }
	    
	 public static boolean isYesterday(Date date) {
		 	Calendar cal = Calendar.getInstance();
		 	cal.add(Calendar.DATE, -1);
	        return isSameDay(date, cal.getTime());
	    }
	 public static boolean isTwoDaysAgo(Date date) {
		 	Calendar cal = Calendar.getInstance();
		 	cal.add(Calendar.DATE, -1);
	        return isBeforeDay(date, cal.getTime()) && isAfterDay(date, lastDayOfLastWeek(Calendar.getInstance()).getTime());
	    }
	 public static boolean isLastWeek(Date date) {
		
		 	Calendar start = lastDayOfLastWeek(Calendar.getInstance());
		 	Calendar end = firstDayOfCurrentMonth(Calendar.getInstance());
		 	if(isSameDay(date, start.getTime()) || isSameDay(date, end.getTime())){
		 		return true;
		 	}
	
		 	
	        return isAfterDay(date, end.getTime()) && isBeforeDay(date, start.getTime());
	    }
	 public static boolean isLastMonth(Date date) {
			
		 	Calendar first = firstDayOfLastMonthMonth(Calendar.getInstance());
		 	Calendar last = lastDayOfLastMonth(Calendar.getInstance());
		 	if(isSameDay(date, first.getTime()) || isSameDay(date, last.getTime())){
		 		return true;
		 	}
	
		 	
	        return isAfterDay(date, first.getTime()) && isBeforeDay(date, last.getTime());
	    }
	 
	public static HashMap<String, ArrayList<Review>> groupDates(ArrayList<Review> reviews){
		
		
		
		HashMap<String, ArrayList<Review>> map = new HashMap<String, ArrayList<Review>>();
		ArrayList<Review> toadyList = new ArrayList<Review>();
		map.put(KEY_TODAY, toadyList);
		ArrayList<Review> yesterDayList = new ArrayList<Review>();
		map.put(KEY_YESTERDAY, yesterDayList);
		ArrayList<Review> twoDaysAgoList = new ArrayList<Review>();
		map.put(KEY_TWO_DAYS_AGO, twoDaysAgoList);
		ArrayList<Review> lastWeekList = new ArrayList<Review>();
		map.put(KEY_LAST_WEEK, lastWeekList);
		ArrayList<Review> lastMontList = new ArrayList<Review>();
		map.put(KEY_LAST_MONTH, lastMontList);
		ArrayList<Review> olderList = new ArrayList<Review>();
		map.put(KEY_OLDER, olderList);
		
		for (int i = 0; i < reviews.size(); i++) {
			Review tranx = reviews.get(i);
			Date date = tranx.date;
			
			if(isToday(date)){
				toadyList.add(tranx);
			}else if (isYesterday(date)) {
				yesterDayList.add(tranx);
			}else if (isTwoDaysAgo(date)) {
				twoDaysAgoList.add(tranx);
			}else if (isLastWeek(date)) {
				lastWeekList.add(tranx);
			}else if (isLastMonth(date)) {
				lastMontList.add(tranx);
			}else{
				olderList.add(tranx);
			}
		}
		
		System.out.println(" " + map.toString());
		
		return map;
	}

}
