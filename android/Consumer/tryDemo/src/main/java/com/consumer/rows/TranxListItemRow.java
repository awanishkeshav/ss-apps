package com.consumer.rows;

import java.text.NumberFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.consumer.R;
import com.consumer.common.SingletonClass;
import com.consumer.models.BackendConstants;
import com.consumer.models.Transaction;
import com.consumer.utility.DateConverter;

public class TranxListItemRow implements TranxListItem {
	public Transaction tranx = null;
	private Context mContext;
/*	private View.OnClickListener reviewButtonClick;
	private View.OnClickListener rowClick;*/
	

	public TranxListItemRow(Context context, Transaction tranx) {
		
		this.tranx = tranx;
		this.mContext = context;
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

		TransactionHolder viewHolder;
		if (convertView == null) {
			viewHolder = new TransactionHolder();
			convertView = inflater.inflate(R.layout.transaction_row, null, false);

			viewHolder.nameTv 	= (TextView) convertView.findViewById(R.id.trNameTv);
			viewHolder.amountTv = (TextView) convertView.findViewById(R.id.trAmountTv);
			viewHolder.dateTv 	= (TextView) convertView.findViewById(R.id.trDateTv);
			viewHolder.tagTv 	= (TextView) convertView.findViewById(R.id.tagTv);
			viewHolder.categoryImgView 	= (ImageView) convertView.findViewById(R.id.categoryImgView);
			viewHolder.reviewImgView = (ImageView) convertView.findViewById(R.id.reviewButton);
			//viewHolder.reviewImgView.setClickable(true);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (TransactionHolder) convertView.getTag();
		}

		viewHolder.nameTv.setText(tranx.getMerchantName());
		viewHolder.dateTv.setText(DateConverter.getStringFromDate(mContext, tranx.date));
		
		viewHolder.amountTv.setText(SingletonClass.getPriceString(tranx.amount));
		String myString = tranx.getTagDisplayString();
		viewHolder.tagTv.setText(myString.length()>0 ? myString.substring(0,1).toUpperCase() + myString.substring(1) : myString);
		
		viewHolder.categoryImgView.setImageResource(BackendConstants.getIconForCategory(tranx.txnCategory));
		
		switch (tranx.reviewStatus) {
		case NO_REVIEW:
			viewHolder.reviewImgView.setVisibility(View.INVISIBLE);
			break;
		case REVIEW_REQUIRED:
			viewHolder.reviewImgView.setVisibility(View.VISIBLE);
			viewHolder.reviewImgView.setImageResource(R.drawable.icon_feedback);
			break;
		case REVIEWED:
			viewHolder.reviewImgView.setVisibility(View.VISIBLE);
			viewHolder.reviewImgView.setImageResource(R.drawable.icon_feedback_blue);
			break;

		}
		
		viewHolder.reviewImgView.setTag(tranx);
		//viewHolder.reviewImgView.setEnabled(true);
		//viewHolder.reviewImgView.setOnClickListener(reviewButtonClick);
		/*viewHolder.reviewImgView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "Review Image", Toast.LENGTH_SHORT).show();
			}
		});*/
		
		if(tranx.tags != null && !tranx.tags.isEmpty()){
			viewHolder.tagTv.setVisibility(View.VISIBLE);
		}else{
			viewHolder.tagTv.setVisibility(View.GONE);
		}
		//convertView.setClickable(true);
		//convertView.setOnClickListener(rowClick);
		
		return convertView;
	}
	
	public class TransactionHolder{
		
		public TextView nameTv, dateTv, amountTv, tagTv;
		public ImageView categoryImgView;
		public ImageView reviewImgView;
	}
}

