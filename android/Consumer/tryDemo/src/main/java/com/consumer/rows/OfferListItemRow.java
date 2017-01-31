package com.consumer.rows;

import java.sql.Date;

import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.consumer.R;
import com.consumer.models.BackendConstants;
import com.consumer.models.Offer;
import com.consumer.models.Offer.Status;
import com.consumer.utility.DateConverter;
import com.consumer.utility.DateUtil;

public class OfferListItemRow implements TranxListItem {
	public Offer offer = null;
	private boolean mShowDistance;
	

	public OfferListItemRow(Offer offer, boolean showDistance) {
		mShowDistance = showDistance;
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
			viewHolder.merchantNameTv 	= (TextView) convertView.findViewById(R.id.mrNameTv);
			viewHolder.titleTv 	= (TextView) convertView.findViewById(R.id.offerTitleTv);
			viewHolder.expireTv 	= (TextView) convertView.findViewById(R.id.offerExpireTv);
			viewHolder.distanceTv 	= (TextView) convertView.findViewById(R.id.distanceTv);
			viewHolder.categoryImgView 	= (ImageView) convertView.findViewById(R.id.categoryImgView);
			viewHolder.distanceLL 	= (LinearLayout) convertView.findViewById(R.id.distanceLL);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (OfferHolder) convertView.getTag();
		}
		
		viewHolder.distanceLL.setVisibility(View.GONE);
		if (mShowDistance) {
			viewHolder.distanceLL.setVisibility(View.VISIBLE);
			viewHolder.distanceTv.setText(offer.distance.toString());
		}
		viewHolder.merchantNameTv.setText(offer.merchant.name);
		viewHolder.titleTv.setText(offer.title);
		if (offer.status == Status.NEW) {
			viewHolder.titleTv.setTextColor(Color.parseColor("#3492CE"));
		}else{
			viewHolder.titleTv.setTextColor(Color.parseColor("#475D88"));
		}
		viewHolder.categoryImgView.setImageResource(BackendConstants.getIconForCategory(this.offer.category));
        if (offer.expiryDate != null){
            viewHolder.expireTv.setText("Expires "+ android.text.format.DateFormat.format("dd/MM/yyyy",offer.expiryDate));

        }else{
            viewHolder.expireTv.setText("");

        }
		return convertView;
	}
	
	public class OfferHolder{
		
		public TextView titleTv, merchantNameTv, expireTv, distanceTv; 
		public ImageView categoryImgView;
		public LinearLayout distanceLL;
	}
}

