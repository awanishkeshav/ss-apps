package com.merchant.rows;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.merchant.R;
import com.merchant.common.SingletonClass;
import com.merchant.models.Offer;
import com.merchant.utility.DateConverter;

public class OfferListItemRow implements MyListItem {
	public Offer offer = null;

	public OfferListItemRow(Offer offer, boolean showDistance) {
		this.offer = offer;
	}

/*	public void setReviewButtonClick(View.OnClickListener reviewButtonClick) {
		this.reviewButtonClick = reviewButtonClick;
	}

	public void setRowClick(View.OnClickListener rowClick) {
		this.rowClick = rowClick;
	}
*/
	@Override
	public int getViewType() {
		// TODO Auto-generated method stub
		return ListItemType.ROW.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub

		OfferHolder viewHolder;
		if (convertView == null) {
			viewHolder = new OfferHolder();
			convertView = inflater.inflate(R.layout.offer_row, null, false);
			viewHolder.dateTv 	= (TextView) convertView.findViewById(R.id.dateTv);
			viewHolder.titleTv 	= (TextView) convertView.findViewById(R.id.offerTitleTv);
			viewHolder.statusTv 	= (TextView) convertView.findViewById(R.id.offerStatusTv);
			viewHolder.usedCntTv 	= (TextView) convertView.findViewById(R.id.usedCntTv);
			viewHolder.usedTv 	= (TextView) convertView.findViewById(R.id.usedTv);
			viewHolder.value$Tv 	= (TextView) convertView.findViewById(R.id.value$Tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (OfferHolder) convertView.getTag();
		}
		
		viewHolder.titleTv.setText(offer.title);
		String dateRangeStr = DateConverter.getStringFromDateMMDDYY(offer.startDate);
		if (offer.expiryDate != null) {
			dateRangeStr = dateRangeStr + " - " + DateConverter.getStringFromDateMMDDYY(offer.expiryDate);
		}
		viewHolder.dateTv.setText(dateRangeStr);
		
		viewHolder.titleTv.setTextColor(Color.parseColor("#777777"));
		String statusStr = "";
		switch (offer.status) {
		case Active:
			statusStr = "";
			viewHolder.titleTv.setTextColor(Color.parseColor("#3492CE"));
			break;
		case InActive:
			statusStr = "Inactive";
			break;
		case Expired:
			statusStr = "Inactive (Expired)";
			break;
		case Archived:
			statusStr = "";
			break;

		default:
			break;
		}
		viewHolder.statusTv.setText(statusStr);
		
		String usedCntStr = " ";
		if (offer.getUsedCount() > 0) {
			usedCntStr = SingletonClass.stringFromNumber(offer.getUsedCount(), 0);
		}
		
		viewHolder.usedCntTv.setText(usedCntStr);
		
		String usedStr = "    ";
		if (offer.getUsedCount() > 0) {
			usedStr = "USED";
		}
		viewHolder.usedTv.setText(usedStr);
		
		String $Str = "";
		if (offer.getValue$() > 0 && offer.getUsedCount() > 0) {
			$Str = SingletonClass.getPriceString(offer.getValue$());
		}
		viewHolder.value$Tv.setText($Str);
		
		
		return convertView;
	}
	
	public class OfferHolder{
		
		public TextView titleTv, dateTv, statusTv, usedCntTv, usedTv, value$Tv; 
		
	}
}

