package com.consumer.models;

import java.io.Serializable;

public class AnalysisVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TxnCategory category;
	public Double expenseThisMonth;
	public Integer expensePercentThisMonth, expensePercentLastMonth;
	
	public AnalysisVO(TxnCategory cat, Double expThisMonth, Integer expPerThisMonth, Integer expPerLastMonth){
		category = cat;
		expenseThisMonth = expThisMonth;
		expensePercentThisMonth = expPerThisMonth;
		expensePercentLastMonth = expPerLastMonth;
	}

}
