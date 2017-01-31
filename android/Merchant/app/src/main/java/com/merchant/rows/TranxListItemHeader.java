package com.merchant.rows;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.merchant.R;

public class TranxListItemHeader implements MyListItem {
	private String title = null;
	private boolean showTopSeperator = false;

	public TranxListItemHeader(String title, boolean showTopSeperator) {
		
		this.title = title;
		this.showTopSeperator = showTopSeperator;
	}
	
	public TranxListItemHeader(String title) {
		
		this.title = title;
	}

	@Override
	public int getViewType() {
		// TODO Auto-generated method stub
		return ListItemType.Header.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub

		View row = convertView;
		if (row == null) {
			row = inflater.inflate(R.layout.transactions_section_header, parent,
					false);	
		}
		
		TextView titleTv = (TextView) row.findViewById(R.id.headerTitleTv);
		View seperator = row.findViewById(R.id.topBlueSeperator);
		titleTv.setText(title);
		if(this.showTopSeperator){
			seperator.setVisibility(View.VISIBLE);
		}else{
			seperator.setVisibility(View.GONE);
		}
		return row;
	}
}