package com.consumer.adapters;

import java.util.List;

import com.consumer.TransDetailActivity;
import com.consumer.common.SingletonClass;
import com.consumer.models.Transaction;
import com.consumer.rows.TranxListItem;
import com.consumer.rows.TranxListItemRow;
import com.consumer.rows.TranxListItem.ListItemType;
import com.consumer.rows.TranxListItemRow.TransactionHolder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class TransactionAdapter extends BaseAdapter{

	private LayoutInflater minflater;
	private List<TranxListItem> transactionList;
	
	private Context mContext;
	
	public TransactionAdapter(Context context) {
		mContext = context;
		minflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return null == transactionList ? 0 : transactionList.size();
	}

	@Override
	public Object getItem(int position) {
		return transactionList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public int getItemViewType(int position) {
		TranxListItem item = (TranxListItem) getItem(position);		
		return item.getViewType();

	}

	@Override
	public int getViewTypeCount() {

		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = null;
		
		TranxListItem item = (TranxListItem) getItem(position);
		row = item.getView(minflater, convertView, parent);
		
		
		
		if(item.getClass() == TranxListItemRow.class){
			TranxListItemRow tranxListItemRow = (TranxListItemRow) item;
		/*	tranxListItemRow.setRowClick(rowClick);
			tranxListItemRow.setReviewButtonClick(reviewButtonClick);*/
			row.setId(position);
			
			
			TransactionHolder viewHolder = (TransactionHolder)row.getTag();
			viewHolder.reviewImgView.setOnClickListener(reviewButtonClick);
			//convertView.setOnClickListener(rowClick);
		}

		return row;
	}
	
	
	
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		Boolean enabled = super.isEnabled(position);
		if (getItemViewType(position) == ListItemType.Header.ordinal()) {
			enabled = false;
		}
		return enabled;
	}
	
/*	OnClickListener rowClick = new OnClickListener() {

		
		@Override
		public void onClick(View v) {
			TranxListItemRow txnRowItem = (TranxListItemRow) getItem(v.getId());
			Intent intent = new Intent(mContext , TransDetailActivity.class);
			intent.putExtra(TransDetailActivity.ExtraTransactionVO, txnRowItem.tranx);
			mContext.startActivity(intent);
		}
	};*/
	
	OnClickListener reviewButtonClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Object txnObj = v.getTag();
			if (txnObj instanceof Transaction) {
				Intent intent = SingletonClass.getIntentForAddReviewActivity(mContext, (Transaction) txnObj);
				mContext.startActivity(intent);
			}
		}
	};
	
	
	public void setList(List<TranxListItem> list){	
		transactionList = list;
		notifyDataSetChanged();
	}
	
}

